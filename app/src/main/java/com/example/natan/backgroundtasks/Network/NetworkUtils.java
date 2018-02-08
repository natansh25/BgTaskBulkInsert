package com.example.natan.backgroundtasks.Network;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.natan.backgroundtasks.Pojo.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by natan on 1/30/2018.
 */

public class NetworkUtils {
    Context mContext;

    final static String CONTACT_URL = "https://api.androidhive.info/json/contacts.json";


    //Fetching the json response

    public static List<Contacts> fetchContactData(URL url) throws JSONException {
        //Log.i("xyz", String.valueOf(url));

        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Contacts> contacts = extractFeaturesFromJson(jsonResponse);
       // ContentValues[] contentValues=data(contacts);
        return contacts;

    }


//Building URL used to query MOVIEDB

    public static URL buildURl() {
        Uri builtUri = Uri.parse(CONTACT_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }


    //Method for getting response from Network

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    //Parsing Json data from the getResponse


    public static List<Contacts> extractFeaturesFromJson(String json) throws JSONException {

        List<Contacts> contacts = new ArrayList<>();






        JSONArray ar = new JSONArray(json);

        // for bulk insert
        ContentValues[] cv = new ContentValues[ar.length()];


        for (int i = 0; i < ar.length(); i++) {

            JSONObject ob = ar.getJSONObject(i);


            String name = ob.getString("name");
            String phone = ob.getString("phone");
            String image = ob.getString("image");
            //Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
            Contacts contacts1 = new Contacts(name, image, phone);
            contacts.add(contacts1);

        }

        return contacts;


    }


    // method for bulk data insert


    public static ContentValues[] data(List<Contacts> contacts)
    {


        String[] data=contacts.toArray(new String[contacts.size()]);
        ContentValues[] cv = new ContentValues[data.length];

        for (int i=0;i<=data.length;i++)
        {

            Log.i("nnn",data[i]);

        }



        return cv;

    }


}
