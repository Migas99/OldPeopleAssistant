package pt.ipp.estg.pharmacies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import pt.ipp.estg.pharmacies.FallsDetector.SensorService;
import pt.ipp.estg.pharmacies.Game.Game;
import pt.ipp.estg.pharmacies.Game.GameMenu;
import pt.ipp.estg.pharmacies.Game.GameScore;
import pt.ipp.estg.pharmacies.Pharmacies.Fragments.ListPharmaciesFragment;
import pt.ipp.estg.pharmacies.Pharmacies.Fragments.MapFragment;
import pt.ipp.estg.pharmacies.medicineReminder.fragments.HistoryListFragment;
import pt.ipp.estg.pharmacies.medicineReminder.fragments.HistoryMapFragment;
import pt.ipp.estg.pharmacies.medicineReminder.fragments.MedicineListFragment;
import pt.ipp.estg.pharmacies.medicineReminder.fragments.ReminderListFragment;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.HistoryListenerInterface;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.MedicineListenerInterface;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.ReminderListenerInterface;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;

public class MainActivity extends AppCompatActivity
        implements MenuPrincipalFragment.OnCityGivenListener, ListPharmaciesFragment.OnPharmacyGivenListener,
        Game.GameListener, GameMenu.MenuGameListener, GameScore.GameScoreListener,
        ReminderListenerInterface, MedicineListenerInterface, HistoryListenerInterface, HistoryListFragment.OnCoordsGivenListener {

    private Toolbar toolbar;
    MenuPrincipalFragment inputFragment;
    MedicineListFragment medicineListFragment;
    ReminderListFragment reminderListFragment;
    HistoryListFragment historyListFragment;

    private SensorService sensorService;
    private Intent sensorServiceIntent;
    private ServiceConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_container);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbar.setTitle("Menu");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.sensorServiceIntent = new Intent(this, SensorService.class);
        this.mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sensorService = ((SensorService.SensorServiceBinder) service).getService();
                sensorService.start();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                sensorService.stop();
            }
        };


        inputFragment = new MenuPrincipalFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, inputFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(this.sensorServiceIntent, this.mConnection, Context.BIND_AUTO_CREATE);
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(this.mConnection);
    }

    @Override
    public void onCityGiven(String city) {
        ListPharmaciesFragment newFragment = new ListPharmaciesFragment();
        Bundle args = new Bundle();
        args.putString("City", city);
        newFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPharmacyGiven(String pharmacy, double latitude, double longitude) {
        MapFragment newFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("Name", pharmacy);
        args.putDouble("Latitude", latitude);
        args.putDouble("Longitude", longitude);
        newFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onCoordsGiven(double latitude, double longitude) {
        HistoryMapFragment newFragment = new HistoryMapFragment();
        Bundle args = new Bundle();
        args.putDouble("Latitude", latitude);
        args.putDouble("Longitude", longitude);
        newFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void startGame(int difficulty) {
        Game game = new Game();
        Bundle details = new Bundle();

        details.putInt("difficulty", difficulty);
        game.setArguments(details);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, game)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void getResults(int difficulty, int numRightQuestions, int numTotalQuestions) {
        GameScore score = new GameScore();
        Bundle details = new Bundle();

        details.putInt("difficulty", difficulty);
        details.putInt("numRightQuestions", numRightQuestions);
        details.putInt("numTotalQuestions", numTotalQuestions);
        score.setArguments(details);

        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, score)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelButton() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void backGameMenu() {
        getSupportFragmentManager().popBackStack();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void OnMenuFragmentInteractionListener(String args) {
        if (args.length() == 0) return;

        switch (args) {
            case "medicineList":
                this.medicineListFragment = new MedicineListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, this.medicineListFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case "reminderList":
                this.reminderListFragment = new ReminderListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, this.reminderListFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case "historyList":
                this.historyListFragment = new HistoryListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, this.historyListFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case "game":
                GameMenu gameMenu = new GameMenu();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, gameMenu)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void openAccount() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void addMedicine(String name, String description, int amount) {
        this.medicineListFragment.addMedicine(name, description, amount);
    }

    @Override
    public void deleteMedicine(String name, String description, int amount) {
        this.medicineListFragment.deleteMedicine(name, description, amount);
    }

    @Override
    public void updateMedicine(String name, String description, int amount) {
        this.medicineListFragment.updateMedicine(name, description, amount);
    }

    @Override
    public void addReminder(int medicine_id, int hour, int minute) {
        this.reminderListFragment.addReminder(medicine_id, hour, minute);
    }

    @Override
    public void addReminder(int medicine_id, int hour, int minute, int hourRepeat) {
        this.reminderListFragment.addReminder(medicine_id, hour, minute, hourRepeat);
    }

    @Override
    public void deleteReminder(int medicine_id, int hour, int minute) {
        this.reminderListFragment.deleteReminder(medicine_id, hour, minute);
    }

    @Override
    public void updateReminder(int medicine_id, int hour, int minute) {
        this.reminderListFragment.updateReminder(medicine_id, hour, minute);
    }

    @Override
    public List<Medicine> getAllMedicine() {
        return this.reminderListFragment.getAllMedicine();
    }

    @Override
    public void insertHistory(int medicineId, double latitude, double longitude, int day, int month, int year, int hour, int minute) {
        this.historyListFragment.insertHistory(medicineId, latitude, longitude, day, month, year, hour, minute);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                onBackPressed();
        }

        return true;
    }
}

