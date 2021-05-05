package ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Constants;
import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Utilities;

public class FTPServerCommunicationAsyncTask extends AsyncTask<String, String, Void> {

    private TextView welcomeMessageTextView;

    public FTPServerCommunicationAsyncTask(TextView welcomeMessageTextView) {
        this.welcomeMessageTextView = welcomeMessageTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        try {
            // TODO exercise 4
            // open socket with FTPServerAddress.getText().toString() (taken from param[0]) and port (Constants.FTP_PORT = 21)
            socket = new Socket(params[0], Constants.FTP_PORT);
            // get the BufferedReader attached to the socket (call to the Utilities.getReader() method)
            BufferedReader bufferedReader = Utilities.getReader(socket);
            // should the line start with Constants.FTP_MULTILINE_STARTCODE = "220-", the welcome message is processed
            String line = bufferedReader.readLine();
            if (line != null && line.startsWith(Constants.FTP_MULTILINE_START_CODE) == true) {
                // read lines from server while
                // - the value is different from Constants.FTP_MULTILINE_END_CODE1 = "220"
                // - the value does not start with Constants.FTP_MULTILINE_END_CODE2 = "220 "
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.equals(Constants.FTP_MULTILINE_END_CODE1) == false && line.startsWith(Constants.FTP_MULTILINE_END_CODE2) == false) {
                        // append the line to the welcomeMessageTextView text view content (on the UI thread !!!) - publishProgress(...)
                        publishProgress(line);
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception exception) {
            Log.d(Constants.TAG, exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        } finally {
            // close the socket
           try  {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioException) {
               if (Constants.DEBUG) {
                   ioException.printStackTrace();
               }
           }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        welcomeMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        // TODO exercise 4
        // append the progress[0] to the welcomeMessageTextView text view
        welcomeMessageTextView.append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}

}
