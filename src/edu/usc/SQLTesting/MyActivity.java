package edu.usc.SQLTesting;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import android.app.ListActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.*;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import sql.benchmark.UnnecessaryColumnRetrieval;

public class MyActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    private DataSource datasource;

    String packageName = "sql.benchmark.";
    String UNBATCHEDWRITES = "UnbatchedWrites";
    String MERGECOLUMN = "NotMergingProjectionPredicates";
    String MERGEROW = "NotMergingSelectionPredicates";

    String LOOPTOJOIN = "LoopToJoin";

    String TAINTEDQUERY = "TaintedQuery";

    String PARAMERIZE = "NotUsingParamerizedQuery";

    String CACHE = "NotCaching";

    String UNNECESSARYCOLUMN = "UnnecessaryColumnRetrieval";

    String UNNECESSARYROW = "UnnecessaryRowRetrieval";

    String READABLEPASSWORD = "ReadablePassword";

    String IGNORERETURN = "IgnoreReturn";

    String UNBOUNDEDQUERY = "UnboundedQuery";

    private static final int coolDownSecond = 1;
    private static final int numOfRepeatedExp = 15;
    private int rowSize[] = {82, 558, 1035};
    private int fieldSize[] = {93, 252, 412};
    private int querySize[] = {6, 69, 133};
    private Queue<Environment> experimentEnvironments = new LinkedList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.usc.SQLTesting.R.layout.main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        File sdcard = android.os.Environment.getExternalStorageDirectory();
        System.setProperty("org.owasp.esapi.resources", sdcard.getAbsolutePath());

        datasource = new DataSource(this);

        Map<String, List<String>> patternToCodeEntryMethods = new LinkedHashMap<>();

        try {
            FileReader fin = new FileReader(sdcard + "/pattern.txt");
            BufferedReader bufRead = new BufferedReader(fin);
            String line = bufRead.readLine();
            while (line != null)
            {
                if(line.contains(":")) {
                    String pattern = line.split(":")[0];
                    String exp = line.split(":")[1];
                    if(patternToCodeEntryMethods.containsKey(pattern))
                        patternToCodeEntryMethods.get(pattern).add(exp);
                    else
                    {
                        List<String> list = new ArrayList<>();
                        list.add(exp);
                        patternToCodeEntryMethods.put(pattern, list);
                    }
                }
                line = bufRead.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


       // patternToCodeEntryMethods.put(UNBATCHEDWRITES, Arrays.asList("experiment1", "experiment2", "experiment3", "experiment4"));

       // patternToCodeEntryMethods.put(MERGECOLUMN, Arrays.asList("experiment1", "experiment2", "experiment3", "experiment4"));

        //patternToCodeEntryMethods.put(MERGEROW, Arrays.asList("experiment1", "experiment2"));

        //patternToCodeEntryMethods.put(LOOPTOJOIN, Arrays.asList("experiment1"));



    //    patternToCodeEntryMethods.put(TAINTEDQUERY, Arrays.asList("experiment1", "experiment2", "experiment3", "experiment4", "experiment5"));

/*
        patternToCodeEntryMethods.put(PARAMERIZE, Arrays.asList("experiment1", "experiment2"));


        patternToCodeEntryMethods.put(CACHE, Arrays.asList("experiment1", "experiment2", "experiment3"));


        patternToCodeEntryMethods.put(UNNECESSARYCOLUMN, Arrays.asList("experiment1", "experiment2", "experiment3", "experiment4", "experiment5", "experiment6", "experiment7", "experiment8", "experiment9", "experiment10", "experiment11", "experiment12", "experiment13", "experiment14"));


        patternToCodeEntryMethods.put(UNNECESSARYROW, Arrays.asList("experiment1", "experiment2", "experiment3", "experiment4", "experiment5"));

        patternToCodeEntryMethods.put(READABLEPASSWORD, Arrays.asList("experiment1", "experiment2"));
        patternToCodeEntryMethods.put(UNBOUNDEDQUERY, Arrays.asList("experiment1", "experiment2", "experiment3", "experiment4", "experiment5", "experiment6", "experiment7", "experiment8", "experiment9"));

        patternToCodeEntryMethods.put(IGNORERETURN, Arrays.asList("experiment1"));
        */

        //System.out.println("HERE" + patternToCodeEntryMethods);
        Set<String> patternNeedToVaryQuerySize = new HashSet<>(Arrays.asList(PARAMERIZE,UNBATCHEDWRITES));

        for(String pattern : patternToCodeEntryMethods.keySet()) {
            //different instances of anti-patterns
            for (String instance : patternToCodeEntryMethods.get(pattern)) {
                for(int row : rowSize)
                {
                    for(int field : fieldSize)
                    {
                        if(patternNeedToVaryQuerySize.contains(pattern))
                        {
                            for(int query : querySize)
                            {
                                addToExperiment(pattern, instance, row, field, query);
                            }
                        }
                        else
                        {
                            addToExperiment(pattern, instance, row, field, -1);
                        }
                    }
                }
            }
        }

        int totalNumOfExperiment = experimentEnvironments.size();
        System.out.println(totalNumOfExperiment * (5 + coolDownSecond) / 3600);
        new CountDownTimer(300000000000L, coolDownSecond * 1000) {

            int i = 0;
            String current = "";
            public void onTick(long millisUntilFinished) {
                Environment exp = experimentEnvironments.poll();
                patternExperiment(exp);
                i++;
                if(i == totalNumOfExperiment || experimentEnvironments.isEmpty())
                {
                    System.out.println("Experiment done!");
                    TextView tv = (TextView) findViewById(R.id.show);
                    tv.setText("Finished");
                    cancel();
                    /*
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);


                    */
                }
                if(!(exp.className + " " + exp.methodName).equals(current))
                {
                    current = exp.className + " " + exp.methodName;
                    TextView tv = (TextView) findViewById(R.id.show);
                    tv.setText(current);
                }
            }

            public void onFinish() {
                System.out.println("done!");

            }
        }.start();



        /*
        List<Comment> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        */


    }

    private void addToExperiment(String pattern, String instance, int row, int field, int query) {

        //origin version
        for(int i = 0; i < numOfRepeatedExp; i++)
        {
            experimentEnvironments.add(new Environment(pattern, instance, row, field, query, false));
        }

        //fix version
        for(int i = 0; i < numOfRepeatedExp; i++)
        {
            experimentEnvironments.add(new Environment(pattern, instance, row, field, query, true));
        }

    }

    private void patternExperiment(Environment exp)
    {
        SQLiteDatabase db = datasource.getSQLiteDatabase();

        try
        {
            Class currentClass = Class.forName(packageName + exp.className);
            Method method = currentClass.getMethod(exp.methodName, SQLiteDatabase.class, Environment.class);
            method.invoke(currentClass.newInstance(), db, exp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        db.close();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }


}
