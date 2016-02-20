package client_side.app2;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String MESSAGES_ENDPOINT = "http://slandr.newr.in";
    EditText messageInput;
    Button sendButton;
    String username;
    Double latitude;
    Double longitude;

    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get our input field by its ID
        messageInput = (EditText) findViewById(R.id.message_input);


        // get our button by its ID
        sendButton = (Button) findViewById(R.id.send_button);

        // set its click listener
        sendButton.setOnClickListener(this);

        messageAdapter = new MessageAdapter(this, new ArrayList<Message>());
        final ListView messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
    }

    @Override
    public void onClick(View v) {
        postMessage();
    }

    private void postMessage()  {

        /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            //get longitude
            double longitude = location.getLongitude();
            //get latitude
            double latitude = location.getLatitude();
        }catch(Exception ex) {
            System.out.println("Exception: " + ex);
        }*/

        String text = messageInput.getText().toString();

        // return if the text is blank
        if (text.equals("")) {
            return;
        }


        RequestParams params = new RequestParams();

        // set our JSON object
        params.put("text", text);
        params.put("name", username);
        params.put("time", new Date().getTime());
        params.put("lat", latitude);
        params.put("lon", longitude);

        // create our HTTP client
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(MESSAGES_ENDPOINT + "/messages", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageInput.setText("");
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
            }
        });

    }
}
