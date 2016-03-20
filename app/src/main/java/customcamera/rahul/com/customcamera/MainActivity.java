package customcamera.rahul.com.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback{

    boolean previewing = false;
    private SurfaceView sv;
    private SurfaceHolder sh;
    private Camera c;
    private Button click;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sv = (SurfaceView)findViewById(R.id.surfaceview);
        sh = sv.getHolder();
        sh.addCallback(this);
        click = (Button)findViewById(R.id.click);
        stop = (Button)findViewById(R.id.stop);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        c.stopPreview();

                        Intent intent = new Intent(getApplicationContext(), Preview.class);
                        intent.putExtra("PHOTO", data);
                        startActivity(intent);

                        overridePendingTransition(R.anim.slideleft, R.anim.fadeout);
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        c = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(previewing){
            c.stopPreview();
            previewing = false;
        }

        if (c != null){
            try {
                c.setPreviewDisplay(sh);
                c.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        c.stopPreview();
        c.release();
        c = null;
    }
}
