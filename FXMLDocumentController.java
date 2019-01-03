package m_player;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.N;
import static javafx.scene.input.KeyCode.P;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import static javafx.scene.input.MouseButton.SECONDARY;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FXMLDocumentController implements Initializable {

    boolean tpy = false, pmt = false;
    @FXML
    public MediaView mv;
    public MediaPlayer mp;
    public Media me;
    @FXML
    Slider volume;
    private FileChooser fc;
    private File selectFile;
    @FXML
    Slider slider;
    @FXML
    public Label st, end;
    int min = 0;
    Statement stt = null;
    Connection con = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // CONNECTING TO DATABASE.
        
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/playlist", "root", "");
            stt = con.createStatement();
            System.out.println("CONNECTION ESTABLISHED");
        } catch (Exception e) {
            System.out.println("Error");
        }
        
        //SELECTING MOST PLAYED SONGS FROM DATABASE.
        
        int counter = 0;

        String query = "select name,path from detail ORDER by most DESC";
        try {
            //ResultSet rs=stt.executeQuery(query);
            ResultSet rs = stt.executeQuery(query);
            while (rs.next()) {
                if (counter == 20) {
                    break;
                }
                counter++;
                map3.put(rs.getString(1), rs.getString(2));
                mostplayed.getItems().add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("89 line error " + e.getMessage());
        }

        /*String query="update detail set rating="+0;
        try {
            stt.executeUpdate(query);
            System.out.println("UPDATED");
        } catch (Exception e) {
            System.out.println("UPDATING ERROR:- "+e.getMessage());
        }*/
        
        //DELETING TRACKING TABLE WHICH IS CARRYIN THE CREATED PLAYLISTS.
        //IF REQUIRED.
        
        
        
        /* query="delete from tracking";
        try{
            stt.executeUpdate(query);
            System.out.println("EXecuted deleted");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
         */
        
        //EXTRACTING THE FILES FROM COMPUTER.
        
        
         /*extract(new File("E:\\"));
        extract(new File("C:\\"));
        extract(new File("F:\\"));*/
    }

    //ADDING THE MOST PLLAYED SONGS IN COMBOBOX.
    
    public void mostPlayedTracker(ActionEvent event) {
        int counter = 0;
        mostplayed.getItems().clear();
        String query = "select name,path from detail ORDER by most DESC";
        try {
            //ResultSet rs=stt.executeQuery(query);
            ResultSet rs = stt.executeQuery(query);
            while (rs.next()) {
                if (counter == 20) {
                    break;
                }
                counter++;
                map3.put(rs.getString(1), rs.getString(2));
                mostplayed.getItems().add(rs.getString(1));
            }
            System.out.println("MOSTPLAYED EXECUTED");
        } catch (Exception e) {
            System.out.println("89 line error " + e.getMessage());
        }

    }

    //PLAYING THE MOST PLAYED SONGS.
    
    public void MOSTPLAYEDPLAYING(ActionEvent event) {
        String path = mostplayed.getValue();
        if (playing) {
            mp.stop();
        }
        if (pa) {
            pl = true;
            pa = false;
        }
        playing = true;
        play(map3.get(path));
    }
    HashMap<String, String> map = new HashMap<String, String>();

    //SEARCHING SONGS BY ARTIST,ALBUM,NAME
    public void search(String s) {

        String ent = null;
        if (s.equals("artist")) {
            ent = JOptionPane.showInputDialog("Artist Name:- ");
        } else if (s.equals("album")) {
            ent = JOptionPane.showInputDialog("Album Name:- ");
        }
        if (s.equals("song")) {
            ent = JOptionPane.showInputDialog("Song Name:- ");
        }
        String query = "select name,path from detail where name REGEXP \"" + ent + "\" ";
        ResultSet rs;
        String ans = "";
        searched.getItems().clear();
        try {
            rs = stt.executeQuery(query);
            System.out.println("QUERY COMPLETED");
            while (rs.next()) {
                ans += rs.getString(1) + "\n";
                map.put(rs.getString(1), rs.getString(2));
                searched.getItems().add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        tpy = false;
        pmt = false;

    }
    @FXML
    ComboBox<String> searched;
    
    //ADDING SEARCHED SONGS TO COMBOBOX.
    
    public void searchedPlay(ActionEvent event) {
        String restore = searched.getValue();
        String pj = map.get(restore);
        if (playing) {
            mp.stop();
        }
        if (pa) {
            pl = true;
            pa = false;
        }
        playing = true;
        searched.getItems().clear();
        searched.getItems().add(restore);
        pmt = false;
        tpy = false;

        play(pj);

    }

    public void search_buttonartist(ActionEvent event) {
        search("artist");
    }

    public void search_buttonalbum(ActionEvent event) {
        search("album");
    }

    public void search_buttonname(ActionEvent event) {
        search("song");
    }

    public void extract(File dir) {
        File l[] = dir.listFiles();

        if (l == null) {
            return;
        }

        for (File x : l) {
            if (x.isDirectory()) {
                extract(x);
            }
            if (x.isHidden() || !x.canRead()) {
                continue;
            } else if (x.getName().endsWith(".mp3") || x.getName().endsWith(".mp4") || x.getName().endsWith(".wmv")||x.getName().endsWith(".wav")) {
                String t = x.getPath();
                String bar = "";
                for (int v = t.length() - 1; v >= 0; v--) {
                    if (t.charAt(v) == '\\') {
                        break;
                    } else {
                        bar += t.charAt(v);
                    }
                }
                String b = "";
                for (int v = bar.length() - 1; v >= 0; v--) {
                    b += bar.charAt(v);
                }
                b = b.toLowerCase();
                String naam = "";
                for (int v = 0; v < t.length(); v++) {
                    if (t.charAt(v) == '\\') {
                        naam += "\\" + "\\";

                    } else {
                        naam += t.charAt(v);
                    }
                }
                //System.out.println(b);
                //String query="delete from detail";
                String query = "insert into detail (path,name,rating,most) values (\"" + naam + "\",\"" + b + "\",\"0\",\"0\")";
                try {
                    int rs = stt.executeUpdate(query);
                    System.out.println("UPDATED ALL THE SONGS IN THE DATABASE: ");
                } catch (Exception e) {
                    System.out.println("INITIALIZE ME DIKKAT");
                }

            }
        }

    }

    @FXML
    private AnchorPane anc;
    public boolean playing = false;
    public boolean pl = true, pa = false;
    public void slow1(ActionEvent event) {
        mp.setRate(0.25);
    }

    public void slow2(ActionEvent event) {
        mp.setRate(0.5);
    }

    public void slow3(ActionEvent event) {
        mp.setRate(0.75);
    }

    public void normal(ActionEvent event) {
        mp.setRate(1);
    }

    public void fast1(ActionEvent event) {
        mp.setRate(1.25);
    }

    public void fast2(ActionEvent event) {
        mp.setRate(1.5);
    }

    public void fast3(ActionEvent event) {
        mp.setRate(2);
    }

    public void stopped() {
        mp.stop();
        pl = false;
        pa = true;
        playing = false;
    }

    public void stop(ActionEvent event) {
        stopped();
    }

    public void close(ActionEvent event) {
        System.exit(0);
    }
    //PLAY/PAUSE THE SONG.
    public void pl_pa(ActionEvent event) {
        //toggle.setSelected(false);
        if (pl == true) {
            mp.pause();
            pl = false;
            pa = true;
            playing = false;
        } else if (pa == true) {
            mp.play();
            pa = false;
            pl = true;
            playing = true;
        }


        
    }
   List<String> str = new ArrayList<String>();
    HashMap<String, String> hm1 = new HashMap<>();
    int m;

    //ADD SONGS (UNSAVED)/(CAN ADD MULTIPLE SONGS AT THE SAME TIME).
    
    public void add(ActionEvent event) {
        tpy = true;
        pmt = false;
        fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("all", "*.mp4", "*.mp3", "*.wav"), new ExtensionFilter("mp4", "*.mp4"), new ExtensionFilter("mp3", "*.mp3"), new ExtensionFilter("wav", "*.wav"));
        List<File> selectFile = fc.showOpenMultipleDialog(null);
        m = str.size();
        String mn;
        for (int l = 0; l < selectFile.size(); l++) {
            mn = "";
            str.add(selectFile.get(l).getAbsolutePath());
            int k = str.get(m).length() - 1;
            while (k >= 0) {
                if (str.get(m).charAt(k) == '\\') {
                    break;
                } else {
                    mn += str.get(m).charAt(k);
                }
                k--;
            }
            String mn1 = "";
            for (k = mn.length() - 1; k >= 0; k--) {
                mn1 += mn.charAt(k);
            }
            mn1=mn1.toLowerCase();
            hm1.put(mn1, str.get(m));
           ls.getItems().add(mn1);
            m++;

        }
        /*TreeMap<String,String> tm=new TreeMap<>();
        tm.putAll(hm1);
        hm1.clear();
        ls.getItems().clear();
        for(Map.Entry<String,String> entry: tm.entrySet())
        {
            hm1.put(entry.getKey(),entry.getValue());
            ls.getItems().add(entry.getKey());
        }*/
        int choose = 0;
        while (selectFile.get(0).getAbsolutePath() != str.get(choose)) {
            choose++;
        }
        if (playing) {
            mp.stop();
            playing = false;
        }
        if (pa) {
            pl = true;
            pa = false;
        }
        playing = true;
        play(str.get(choose));
    }
    
    
    
    
    

    //ADDING SONGS TO PLAYLIST(SAVED) IN DATABASE.
    
    
    public void addToPerm(ActionEvent event) {
        String f = " ";
        String query = "select * from tracking";
        try {
            ResultSet rs = stt.executeQuery(query);
            while (rs.next()) {
                f += "\n" + rs.getString(1);
                //rs.next();
            }
        } catch (Exception e) {
            System.out.println("Abhishek pagal hai");
        }
        ch = JOptionPane.showInputDialog(null, "Choose Playlist: " + f, "");
        int flag = 1;
        System.out.println("ch me " + ch);
        if (ch == null || ch == "" || ch == "\n") {
            JOptionPane.showMessageDialog(null, "nhi choose kiya kuch");

        } else {
            fc = new FileChooser();
            //fc.setInitialDirectory(new File("C:\\Users\\Abhishek\\Music"));
            fc.getExtensionFilters().addAll(new ExtensionFilter("all", "*.mp4", "*.mp3", "*.wav"), new ExtensionFilter("mp4", "*.mp4"), new ExtensionFilter("mp3", "*.mp3"), new ExtensionFilter("wav", "*.wav"));
            List<File> fl = new ArrayList<File>();
            fl = fc.showOpenMultipleDialog(null);
            for (int j = 0; j < fl.size(); j++) {
                String path = fl.get(j).getAbsolutePath();
                String h = "";
                for (int g = 0; g < path.length(); g++) {
                    if (path.charAt(g) == '\\') {
                        h += "\\" + "\\";
                    } else {
                        h += path.charAt(g);
                    }
                }
                System.out.println(h);
                query = "insert into " + ch + " values (\"" + h + "\")";

                try {
                    stt.executeUpdate(query);
                } catch (Exception e) {
                    System.out.println("QUERY NOT COMPLETED IN ADDPERMM FUNCTION");
                }
            }
        }
    }

    int i = 0;
    //NEXT SONG.
    public void nexting() {

        if (ch == null) {
            if (str.size() - 1 == i) {
                mp.stop();
                i = 0;
                play(str.get(i));
            } else {
                mp.stop();
                i = i + 1;
                play(str.get(i));
            }
        } else {
            if (playli.size() - 1 == i) {
                mp.stop();
                i = 0;
                playlies(playli.get(i));
            } else {
                mp.stop();
                i = i + 1;
                playlies(playli.get(i));
            }

        }
    }

    public void next(ActionEvent event) {
        nexting();
    }
    //PREVIOUS SONG.
    public void previous() {
        if (ch == null) {
            if (i == 0) {
                mp.stop();
                i = str.size() - 1;
                play(str.get(i));
            } else {
                mp.stop();
                i = i - 1;
                play(str.get(i));
            }
        } else {
            if (i == 0) {
                mp.stop();
                i = playli.size() - 1;
                playlies(playli.get(i));
            } else {
                mp.stop();
                i = i - 1;
                playlies(playli.get(i));
            }
        }
    }

    public void prev(ActionEvent event) {
        previous();
    }
    int minute = 0;
    int sec = 0;
    
    //PLAY UNSAVED SONGS.
    
    public void play(String mediaFile) {
        /*if(i==str.size())
        {
            return;
        }*/
        current = mediaFile;
        System.out.println(current);
        String h = "";

        for (int j = current.length() - 1; j >= 0; j--) {
            if (current.charAt(j) == '\\') {
                break;
            } else {
                h += current.charAt(j);
            }
        }
        String and = "";
        for (int j = h.length() - 1; j >= 0; j--) {
            and += h.charAt(j);
        }
        int m = 0;
        String query = "select most from detail where name=(\"" + and + "\")";
        try {
            ResultSet rs = stt.executeQuery(query);
            if (rs.next()) {
                //System.out.println(rs.getString(1));
                m = Integer.parseInt(rs.getString(1));
                System.out.println(m);
            }
        } catch (Exception e) {
            System.out.println("686 line error " + e.getMessage());
        }
        m += 1;
        query = "update detail set most=" + m + " where name=(\"" + and + "\")";
        try {
            stt.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("709 line error");
        }
        Media media = new Media(new File(mediaFile).toURI().toString());
        mp = new MediaPlayer(media);
        mv.setMediaPlayer(mp);
        mp.play();
        slider.maxProperty().bind(Bindings.createDoubleBinding(() -> mp.getTotalDuration().toMinutes(), mp.totalDurationProperty()));

        mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
                -> {
            slider.setValue(newValue.toMinutes());
            Double val = slider.getValue() * 0.6;
            int hour = (int) (val * 100);
            Double starting;
            sec = (int) (hour % 60);
            minute=hour/60;
           starting = minute + sec / 100.0;
            st.setText(String.valueOf(String.format("%.2f", starting)));
            Double ftime = slider.getMax();
            Double peechle = ftime * 100;
            int z1 = (int) (peechle / 100);
            peechle = peechle % 100;//0.6;
            peechle = peechle * 0.6;
            Double ans = z1 + peechle / 100;
            end.setText(String.valueOf(String.format("%.2f", ans)));
        });
        slider.setOnMouseDragged((new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                mp.seek(Duration.minutes(slider.getValue()));
            }

        }));
        slider.setOnMouseClicked((new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                mp.seek(Duration.minutes(slider.getValue()));
            }

        }));
        //end.setText(String.valueOf(String.format("%.2f",me.getDuration().toMinutes())));
        System.out.println(mp.getTotalDuration());
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
        volume.setValue(mp.getVolume() * 100);
        volume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp.setVolume(volume.getValue() / 100);

            }
        });

        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                mp.stop();
                //if(flag)
                //     Collections.shuffle(str);
                if (flag) {
                    play(str.get((++i) % randomNumberInRange(1, str.size())));
                } else {
                    play(str.get(++i));
                }
//return ;
            }

        });
    }
    //SHUFFLING SONGS FUNCTION.
    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
    private ChangeListener<Duration> progressChangeListener;
    
    //PLAY UNSAVED SEQUENTIALLY.
    
    public void playAll(ActionEvent event) {
        //play(itr.next());
        if (playing) {
            mp.stop();
        }
        if (pa) {
            pl = true;
            pa = false;
        }
        playing = true;
        // playing=true;
        for (int k = 0; k < temp.size(); k++) {
            str.add(temp.get(k));
        }
        flag = false;
        pmt = false;
        tpy = true;
        play(str.get(i));

    }
    @FXML
    HBox box;

    public void labelVisible(javafx.scene.input.MouseEvent event) {
        //end.setOpacity(1);
        anc.setOpacity(1);
        //box.setOpacity(1);

    }

    public void labelInVisible(javafx.scene.input.MouseEvent event) {
        //end.setOpacity(1);
        anc.setOpacity(0);
        //box.setOpacity(0);
    }
    List<String> temp = new ArrayList<String>();
    Random rand = new Random();
    boolean flag = false;

    //SHUFFLE SONGS.
    
    public void buttrand(ActionEvent event) {
        for (int g = 0; g < str.size(); g++) {
            temp.add(str.get(g));

        }
        Collections.shuffle(str);
        Collections.shuffle(playli);
        flag = true;
    }
    ListView<String> lstvw1;
    ArrayList<String> track = new ArrayList<String>();

    //CREATING PLAYLIST.
    
    public void create(ActionEvent event) {

        String p1 = JOptionPane.showInputDialog("Name of Playlist");
        String query1 = "create table " + p1 + " (path TEXT)";
        String query2 = "insert into tracking (name) values (\"" + p1 + "\");";
        try {

            stt.executeUpdate(query1);
            stt.execute(query2);
            System.out.println("QUERY COMPLETED");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    String ch;

    ArrayList<String> playli = new ArrayList<String>();
    @FXML
    ComboBox<String> selectp;
    HashMap<String, String> hmap = new HashMap<String, String>();
    
    //PLAY FUNCTION FOR PLAYLIST.
    
    public void play_playlist(ActionEvent event) {
        String f = "";
        String query = "select * from tracking";
        try {
            ResultSet rs = stt.executeQuery(query);
            while (rs.next()) {
                f += "\n" + rs.getString(1);
                //rs.next();
            }
        } catch (Exception e) {
            System.out.println("956 line error");
        }

        ch = JOptionPane.showInputDialog("Choose Playlist: " + f);
        if (ch != null) {
            selectp.getItems().clear();
        }
        query = "select path from " + ch;
        try {
            ResultSet rs = stt.executeQuery(query);

            while (rs.next()) {
                String path = rs.getString(1);
                playli.add(rs.getString(1));
                String h = "";
                for (int j = path.length() - 1; j >= 0; j--) {
                    if (path.charAt(j) == '\\') {
                        break;
                    } else {
                        h += path.charAt(j);
                    }
                }
                //playli.get(i);
                String ans = "";
                for (int j = h.length() - 1; j >= 0; j--) {
                    ans += h.charAt(j);
                }
                hmap.put(ans, path);
                selectp.getItems().add(ans);
            }

            flag = false;
            pmt = true;
            tpy = false;
            if (playing) {
                mp.stop();
            }
            playing = true;
            i = 0;
            playlies(playli.get(i));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //SELECTED (SAVED) SONGS PLAY FUNCTION. 
    
    public void selectedplaylistplay(ActionEvent event) {
        String restore = selectp.getValue();
        String path = hmap.get(restore);
        if (playing) {
            mp.stop();
        }
        if (pa) {
            pl = true;
            pa = false;
        }

        playing = true;
        playlies(path);
    }
    String current = "";

    //MAIN METHOD TO PLAY SAVED SONGS.
    
    public void playlies(String mediaFile) {
        current = mediaFile;
        System.out.println(current);
        String h = "";

        for (int j = current.length() - 1; j >= 0; j--) {
            if (current.charAt(j) == '\\') {
                break;
            } else {
                h += current.charAt(j);
            }
        }
        String and = "";
        for (int j = h.length() - 1; j >= 0; j--) {
            and += h.charAt(j);
        }
        int m = 0;
        String query = "select most from detail where name=(\"" + and + "\")";
        try {
            ResultSet rs = stt.executeQuery(query);
            if (rs.next()) {
                //System.out.println(rs.getString(1));
                m = Integer.parseInt(rs.getString(1));
                System.out.println(m);
            }
        } catch (Exception e) {
            System.out.println("1048 line error " + e.getMessage());
        }
        m += 1;
        query = "update detail set most=" + m + " where name=(\"" + and + "\")";
        try {
            stt.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("1057 line error");
        }
        Media media = new Media(new File(mediaFile).toURI().toString());
        mp = new MediaPlayer(media);
        mv.setMediaPlayer(mp);
        mp.play();
        slider.maxProperty().bind(Bindings.createDoubleBinding(() -> mp.getTotalDuration().toMinutes(), mp.totalDurationProperty()));

        mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
                -> {
            slider.setValue(newValue.toMinutes());
            st.setText(String.valueOf(String.format("%.2f", slider.getValue())));
            Double ftime = slider.getMax();
            end.setText(String.valueOf(String.format("%.2f", ftime)));
        });
        slider.setOnMouseDragged((new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                mp.seek(Duration.minutes(slider.getValue()));
            }

        }));
        slider.setOnMouseClicked((new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                mp.seek(Duration.minutes(slider.getValue()));
            }

        }));
        //end.setText(String.valueOf(String.format("%.2f",me.getDuration().toMinutes())));
        System.out.println(mp.getTotalDuration());
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
        volume.setValue(mp.getVolume() * 100);
        volume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp.setVolume(volume.getValue() / 100);

            }
        });

        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mp.stop();
                if (flag == true) {
                    Collections.shuffle(playli);
                }
                playlies(playli.get((++i) % (playli.size())));
                //return ;
            }

        });
    }

    //DRAGGED FUNCIONS 
    public void handleOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            System.out.println("Error in handleOver");
        }
    }
    public ComboBox<String> ls;

     //DROPPED SONGS TO MEDIAPLAYER.
    public void handleDrop(DragEvent event) {
        List<File> lst = event.getDragboard().getFiles();
        List<String> ls1 = new ArrayList<String>();
        m = str.size();
        String mn;
        for (int l = 0; l < lst.size(); l++) {
            mn = "";
            str.add(lst.get(l).getAbsolutePath());
            int k = str.get(m).length() - 1;
            while (k >= 0) {
                if (str.get(m).charAt(k) == '\\') {
                    break;
                } else {
                    mn += str.get(m).charAt(k);
                }
                k--;
            }
            String mn1 = "";
            for (k = mn.length() - 1; k >= 0; k--) {
                mn1 += mn.charAt(k);
            }
            mn1=mn1.toLowerCase();
            hm1.put(mn1, str.get(m));
            ls.getItems().add(mn1);
            m++;

            //ls.getItems().add(selectFile.get(l).getAbsolutePath());
        }
        /*TreeMap<String,String> tm=new TreeMap<>();
        tm.putAll(hm1);
        hm1.clear();
        ls.getItems().clear();
        for(Map.Entry<String,String> entry: tm.entrySet())
        {
            hm1.put(entry.getKey(),entry.getValue());
            ls.getItems().add(entry.getKey());
        }*/

    }
    public void sort(ActionEvent event)
    {
        TreeMap<String,String> tm=new TreeMap<>();
        tm.putAll(hm1);
        hm1.clear();
        ls.getItems().clear();
        for(Map.Entry<String,String> entry: tm.entrySet())
        {
            hm1.put(entry.getKey(),entry.getValue());
            ls.getItems().add(entry.getKey());
        }
    }
    //PLAYING DRAGGED SONGS.
    
    public void playDragged(ActionEvent event) {
        String path = hm1.get(ls.getValue());
        int choose = 0;
        while (path != str.get(choose)) {
            choose++;
        }
        if (playing) {
            mp.stop();
        }
        if (pa) {
            pl = true;
            pa = false;
        }
        playing = true;
        tpy = true;
        pmt = false;
        play(str.get(choose));

    }

    public void visibleMV(ActionEvent event) {
        mv.setOpacity(1);
    }

    public void InvisibleMV(ActionEvent event) {
        mv.setOpacity(0);
    }

    
    
    public void addrating(ActionEvent event) {
        if (playing) {
            while (true) {
                String txt = JOptionPane.showInputDialog("RATE THIS SONG AT A SCALE OF 5");
                //String query="update";
                if (Integer.parseInt(txt) > 5) {
                    JOptionPane.showMessageDialog(null, "PLEASE RATE AT A SCALE OF 5");
                    continue;
                } else {
                    int r = Integer.parseInt(txt);
                    String h = "";
                    for (int g = 0; g < current.length(); g++) {
                        if (current.charAt(g) == '\\') {
                            h += "\\" + "\\";
                        } else {
                            h += current.charAt(g);
                        }
                    }
                    String query = "update detail set rating=" + r + " where path=(\"" + h + "\")";
                    try {
                        stt.executeUpdate(query);
                        System.out.println("RATING IS UPDATED");
                    } catch (Exception e) {
                        System.out.println("ADD RATING ERROR" + e.getMessage());
                    }
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "PLAY SONG FIRST");
        }
    }

    public void getrating(ActionEvent event) {
        if (playing) {
            //String path = null;

            String h = "";
            for (int g = 0; g < current.length(); g++) {
                if (current.charAt(g) == '\\') {
                    h += "\\" + "\\";
                } else {
                    h += current.charAt(g);
                }
            }
            String query = "select rating from detail where path=\"" + h + "\" ";
            try {
                ResultSet rs = stt.executeQuery(query);
                String ans = "";
                while (rs.next()) {
                    ans += rs.getString(1);
                }
                JOptionPane.showMessageDialog(null, "RATING:- " + ans);
                System.out.println("RATING IS SHOWN");
            } catch (Exception e) {
                System.out.println("GET RATING ERROR" + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "PLAY SONG FIRST");
        }
    }
    int space = 0;

    //PLAY/PAUSE USING MOUSE.
    
    public void pauseClicked(MouseEvent event) {
        if (pl==true) {
            mp.pause();
            pa = true;
            pl = false;
            
        } else if(pa==true){
            mp.play();
            pl = true;
            pa = false;
            
        }
    }
    int sp = 0;
    int vol = 0;

    //KEYBOARD EVENT.
    
    public void previousPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.A) {
            System.out.println("P IS PRESSED");
            previous();
        } else if (event.getCode() == KeyCode.D) {
            System.out.println("NEXT IS PRESSED");
            nexting();
        } else if (event.getCode() == S) {
            System.out.println("S IS PRESSED");
            sp++;
            stopped();
            
        } else if (event.getCode() == KeyCode.P) {
            if (pl==true) {
                mp.pause();
                pl = false;
                pa = true;
            } else if(pa==true){
                mp.play();
                pl = true;
                pa = false;
                
            }
        } else if (event.getCode() == KeyCode.M) {
            if (vol % 2 == 0) {
                mp.setVolume(0);
                volume.setValue(0);
                vol++;
            } else {
                mp.setVolume(100);
                volume.setValue(100);
                vol++;
            }
        }
    }
    public void deleteunsaved(MouseEvent event)
    {
        if(event.getButton()==SECONDARY)
        {
            String choice=JOptionPane.showInputDialog("Do you want to delete this song?(y or n)");
            if(choice.equals('y'))
            {
            String f="";
            f=ls.getValue();
            ls.getItems().remove(f);
            hm1.remove(f);
            }
        }
    }
    //SHOWING SHORTCUTS.
    
    public void shortcuts(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "1: press 'p' for Play/pause" + "\n" + "2: press 'A' for go to previous song" + "\n" + "3: press 'D' for go to next song" + "\n" + "4: press 'S' for stop playling the song" + "\n" + "5: press 'F' to MAXIMIZE/MINIMIZE THE SCREEN");
    }
    HashMap<String, String> map3 = new HashMap<String, String>();
    @FXML
    ComboBox<String> mostplayed;

}
