package com.example.calendarproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button add, remove;
    RecyclerView eventlistDisplay;
    ArrayList<Dates> datesWithEvents;
    Dates selectedDate;

    String EventName;
    boolean dateInList;

    int index = -1;

    int indexToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendarView = findViewById(R.id.calendarView);
        add = findViewById(R.id.button2);
        remove = findViewById(R.id.button);
        eventlistDisplay = findViewById(R.id.recyclerView);
        eventlistDisplay.setLayoutManager(new LinearLayoutManager(this));
        datesWithEvents = new ArrayList<>();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
        String DateToday = sdf.format(c);
        Log.d("DATE",DateToday);

        //Getting the date of today so that the selected date can be set to today by default

        int currentDay = Integer.parseInt(DateToday.substring(0,2));
        int currentMonth;
        int currentYear;
        if(DateToday.length() == 9) {
            currentMonth = Integer.parseInt(DateToday.substring(3, 4));
            currentYear = Integer.parseInt(DateToday.substring(5));
        }
        else{
            currentMonth = Integer.parseInt(DateToday.substring(3, 5));
            currentYear = Integer.parseInt(DateToday.substring(6));
        }

        selectedDate = new Dates(currentYear,currentMonth-1,currentDay);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateInList = false;
                Log.d("DATE SELECTED", month+"-"+dayOfMonth+"-"+year);

                //Checking to see if the day selected is in the datesWithEvents
                //If it isn't, then we'll make a new date object when appropriate by creating a new instance, setting it equal to the selected date and adding it to the list

                String dateName = month+"-"+dayOfMonth+"-"+year;
                for(int i = 0; i < datesWithEvents.size(); i++){
                    if(datesWithEvents.get(i).getName().equals(dateName)) {
                        selectedDate = datesWithEvents.get(i);
                        dateInList = true;
                        Log.d("CHECKING IF DATE EXISTS", "IT MATCHES");
                        index = i;
                    }
                }
                if(!dateInList) {
                    selectedDate = new Dates(year, month, dayOfMonth);
                    Log.d("CHECKING IF DATE EXISTS", "IT'S NOT THERE");
                }

                Log.d("Events on this day", selectedDate.getEventList().toString());
                CustomAdapter adapter = new CustomAdapter(selectedDate.getEventList());
                eventlistDisplay.setAdapter(adapter);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = createAddDialog();
                dialog.show();

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Checking to see if the selected date has at least one event so that the remove event function can be called

                if(selectedDate.getEventList().size()>0) {
                    AlertDialog dialog = createRemoveDialogue();
                    dialog.show();
                }
                else{
                    Toast.makeText(MainActivity.this, "There has to be events on this day to remove it", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    AlertDialog createAddDialog(){

        //setting wha the alert dialogue says and adding the edittext

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Event Name:");
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EventName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setPositiveButton("Create Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CustomAdapter adapter;

                //If the date is already in the arraylist, then just adjust that object; Otherwise, add the new selected day into the arraylist as a new object

                if(dateInList){
                    datesWithEvents.get(index).AddEvent(EventName);
                    adapter = new CustomAdapter(datesWithEvents.get(index).getEventList());
                }
                else{
                    selectedDate.AddEvent(EventName);
                    Dates s = selectedDate;
                    datesWithEvents.add(s);
                    index = datesWithEvents.size()-1;
                    adapter = new CustomAdapter(datesWithEvents.get(index).getEventList());
                }
                Log.d("EVENT_LIST_UPDATED", datesWithEvents.get(index).getEventList().toString());
                eventlistDisplay.setAdapter(adapter);
                EventName = "";
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventName = "";
            }
        });
        return builder.create();
    }

    AlertDialog createRemoveDialogue(){

        //dialog text and edit text

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Index of Event:");
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // checking to see if it's an integer (so that the conversion from Int to String is possible)

                try {
                    indexToRemove = Integer.parseInt(charSequence.toString());
                } catch (NumberFormatException e) {
                    if(charSequence.toString().length()>0) {
                        //Toast.makeText(MainActivity.this, "Must be an integer", Toast.LENGTH_SHORT).show();
                        indexToRemove = -1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        builder.setPositiveButton("Remove Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    if(indexToRemove >= 0) {
                        datesWithEvents.get(index).RemoveEventAt(indexToRemove);
                        CustomAdapter adapter = new CustomAdapter(datesWithEvents.get(index).getEventList());
                        eventlistDisplay.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Event Canceled (Must put integer)", Toast.LENGTH_SHORT).show();
                    }
                }catch (IndexOutOfBoundsException e){
                    Toast.makeText(MainActivity.this, "Index must be in the range of 0-"+(datesWithEvents.get(index).getEventList().size()-1), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }


}