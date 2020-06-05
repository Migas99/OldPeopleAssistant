package pt.ipp.estg.pharmacies.Pharmacies.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class RetrievePharmacyIconTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;

    public RetrievePharmacyIconTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bmp = null;

        try {
            URL url = new URL(strings[0]);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {

        }

        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        super.onPostExecute(bmp);
        this.imageView.setImageBitmap(bmp);
    }
}
