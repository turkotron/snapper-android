package org.turkotron.snapper;

import android.app.Activity;
import android.os.Bundle;
import java.io.File;
import android.util.Log;
// import java.net.*;
// import java.io.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.content.FileBody;

public class MainActivity extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    Log.i("turkotron", "=========================================================");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    Log.i("turkotron", "=========================================================");
    // listf("/");

    String filePath = "/sdcard/DCIM/.thumbnails/1401918553185.jpg";
    Log.i("turkotron", filePath);
    try {
      upload("http://turkotron.lichess.org/snapper/upload", new File(filePath));
    } catch(Exception e) {
      Log.e("turkotron", e.getMessage());
    }
  }

  public void upload(String url, File file) throws Exception {
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(url);

    HttpEntity entity = MultipartEntityBuilder.create()
      .addPart("image", new FileBody(file)).build();
    post.setEntity(entity);

    HttpResponse response = client.execute(post);
  }

  // public void upload(String urlToConnect, String filePath) throws Exception {
  //   String paramToSend = "image";
  //   File fileToUpload = new File(filePath);
  //   String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.

  //   URLConnection connection = new URL(urlToConnect).openConnection();
  //   connection.setDoOutput(true); // This sets request method to POST.
  //   connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
  //   PrintWriter writer = null;
  //   try {
  //     writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));

  //     writer.println("--" + boundary);
  //     writer.println("Content-Disposition: form-data; name=\"image\"");
  //     writer.println("Content-Type: text/plain; charset=UTF-8");
  //     writer.println();
  //     // writer.println(paramToSend);

  //     // writer.println("--" + boundary);
  //     // writer.println("Content-Disposition: form-data; name=\"image\"; filename=\"image.jpg\"");
  //     // writer.println("Content-Type: text/plain; charset=UTF-8");
  //     // writer.println();
  //     BufferedReader reader = null;
  //     try {
  //       reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToUpload), "UTF-8"));
  //       for (String line; (line = reader.readLine()) != null;) {
  //         writer.println(line);
  //       }
  //     } finally {
  //       if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
  //     }

  //     writer.println("--" + boundary + "--");
  //   } finally {
  //     if (writer != null) writer.close();
  //   }

  //   // Connection is lazily executed whenever you request any status.
  //   int responseCode = ((HttpURLConnection) connection).getResponseCode();
  //   Log.i("turkotron", "HTTP response: " + new Integer(responseCode).toString()); // Should be 200
  // }

  public void listf(String directoryName) {
    File directory = new File(directoryName);

    // get all the files from a directory
    File[] fList = directory.listFiles();
    for (File file : fList) {
      Log.i("turkotron", file.getAbsolutePath());
      if (file.isFile()) {
      } else if (file.isDirectory()) {
        listf(file.getAbsolutePath());
      }
    }
  }
}
