package pt.ipp.estg.pharmacies.Game;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import pt.ipp.estg.pharmacies.R;

public class Game extends Fragment {
    ArrayList<String> questions;
    TextView equacao;
    EditText userInput;
    TextView timer;
    Button calculateButao;
    Game.GameListener listener;
    Button cancelButton;
    int difficulty;
    int solution;
    int numRightAnswers;
    String question;
    int numTotalQuestions;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.game, container, false);

        questions = new ArrayList<>();
        equacao = view.findViewById(R.id.equacao);
        calculateButao = view.findViewById(R.id.calculate);
        userInput = view.findViewById(R.id.userInput);
        timer = view.findViewById(R.id.timer);
        cancelButton = view.findViewById(R.id.cancelGame);

        difficulty = getArguments().getInt("difficulty");
        question = generateEquation();
        equacao.setText(question);
        questions.add(question);

        if(difficulty == 1){
            timeLeftInMillis = 60000;
        }
        else if(difficulty == 2){
            timeLeftInMillis = 120000;
        }else if(difficulty == 3){
            timeLeftInMillis = 240000;
        }

        startCountDown();

        calculateButao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userInput.getText().toString().equals("")){
                    if (solution == Integer.valueOf(userInput.getText().toString())) {
                        numRightAnswers++;
                        Toast.makeText(getActivity(),"Correto", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"Errado",Toast.LENGTH_LONG).show();
                    }
                    numTotalQuestions++;
                    question = generateEquation();
                    equacao.setText(question);
                    questions.add(question);
                    userInput.setText("");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancelButton();
            }
        });

        return view;
    }

    public void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                listener.getResults(difficulty, numRightAnswers, questions.size()-1);
            }
        }.start();
    }

    public void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis/1000) / 60;
        int seconds = (int) (timeLeftInMillis/1000) %60;

        String timeFormatter = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        timer.setText(timeFormatter);
    }

    public String generateEquation() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        Double result = 1001.1;
        String printEquacao = "";
        String calculateEquacao = "";
        Random random = new Random();
        int operators = 0;
        int operations = 0;
        solution = 1001;

        if (difficulty == 1) {
            operators = 2;
            operations = 2;
        } else if (difficulty == 2) {
            operators = 4;
            operations = 3;
        } else if (difficulty == 3) {
            operators = 4;
            operations = 4;
        }

        while ((solution <= 0 || solution > 1000) || (result != (double) solution)) {
            solution = 0;
            printEquacao = "" + (random.nextInt(19) + 1);
            calculateEquacao = printEquacao;

            for (int i = 0; i < operations; i++) {
                if (i != (operations - 1) && random.nextInt(4) == 1) {
                    String[] operator1 = randomOperator(operators);
                    String[] operator2 = randomOperator(operators);
                    if (difficulty == 3) {
                        if ((random.nextInt(3) + 1) == 1) {
                            String[] value1 = randomHardOperation();
                            String[] value2 = randomHardOperation();
                            printEquacao = printEquacao + operator1[0] + "(" + value1[0] + operator2[0] + value2[0] + ")";
                            calculateEquacao = calculateEquacao + operator1[1] + "(" + value1[1] + operator2[1] + value2[1] + ")";
                        } else {
                            String value1 = "" + (random.nextInt(19) + 1);
                            String[] value2 = randomHardOperation();
                            printEquacao = printEquacao + operator1[0] + "(" + value1 + operator2[0] + value2[0] + ")";
                            calculateEquacao = calculateEquacao + operator1[1] + "(" + value1 + operator2[1] + value2[1] + ")";
                        }
                    } else {
                        String value1 = "" + (random.nextInt(19) + 1);
                        String value2 = "" + (random.nextInt(19) + 1);
                        printEquacao = printEquacao + operator1[0] + "(" + value1 + operator2[0] + value2 + ")";
                        calculateEquacao = calculateEquacao + operator1[1] + "(" + value1 + operator2[1] + value2 + ")";
                    }
                    i++;
                } else {
                    String[] operator1 = randomOperator(operators);
                    if (difficulty == 3) {
                        if ((random.nextInt(3) + 1) == 1) {
                            String[] values = randomHardOperation();
                            String value = values[1];
                            String printValue = values[0];
                            printEquacao = printEquacao + operator1[0] + printValue;
                            calculateEquacao = calculateEquacao + operator1[1] + value;
                        } else {
                            String value = "" + (random.nextInt(19) + 1);
                            printEquacao = printEquacao + operator1[0] + value;
                            calculateEquacao = calculateEquacao + operator1[1] + value;
                        }
                    } else {
                        String value = "" + (random.nextInt(19) + 1);
                        printEquacao = printEquacao + operator1[0] + value;
                        calculateEquacao = calculateEquacao + operator1[1] + value;
                    }
                }
            }
            try {
                result = (double) engine.eval(calculateEquacao);
                solution = result.intValue();
                System.out.println("RESULTADO: " + solution);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        return printEquacao;
    }

    public String[] randomOperator(int operators) {
        Random random = new Random();
        int operator = random.nextInt(operators) + 1;
        String[] operadores = new String[2];
        switch (operator) {
            case 1:
                operadores[0] = "+";
                operadores[1] = "+";
                return operadores;
            case 2:
                operadores[0] = "-";
                operadores[1] = "-";
                return operadores;
            case 3:
                operadores[0] = "x";
                operadores[1] = "*";
                return operadores;
            case 4:
                operadores[0] = "/";
                operadores[1] = "/";
                return operadores;
            default:
                return operadores;
        }
    }

    public String[] randomHardOperation() {
        Random random = new Random();
        int operator = random.nextInt(2) + 1;
        String[] operadores = new String[2];
        switch (operator) {
            case 1:
                int value = (random.nextInt(9) + 1);
                operadores[0] = "(" + value + "^" + 2 + ")";
                operadores[1] = "Math.pow" + "(" + value + "," + 2 + ")";
                return operadores;
            case 2:
                int value2 = 2;
                int result = (int) Math.sqrt(value2);
                while ((Math.sqrt(value2) - (double) result) != 0.0) {
                    value2 = (random.nextInt(99) + 1);
                    result = (int) Math.sqrt(value2);
                }
                operadores[0] = "\u221A" + value2;
                operadores[1] = "Math.sqrt" + "(" + value2 + ")";
                return operadores;
            default:
                return operadores;
        }
    }

    public interface GameListener{
        void getResults(int difficulty, int numRightQuestions, int numTotalQuestions);
        void cancelButton();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GameMenu.MenuGameListener) {
            listener = (Game.GameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
