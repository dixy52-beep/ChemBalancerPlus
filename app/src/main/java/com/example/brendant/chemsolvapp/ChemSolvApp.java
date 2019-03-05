package com.example.brendant.chemsolvapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;

public class ChemSolvApp extends AppCompatActivity {

    private EditText entry;
    private Button solveButton;
    private TextView soln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chem_solv_app);

        entry = (EditText)findViewById(R.id.entry);
        solveButton = (Button)findViewById(R.id.solve_button);
        soln = (TextView)findViewById(R.id.solution_view);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Solve the equation and update text view with solution.
                String inputText = new String(entry.getText().toString());
                entry.onEditorAction(EditorInfo.IME_ACTION_DONE);       //close android keyboard
                String solutionText = run(inputText);
                soln.setText(solutionText);
            }
        };

        solveButton.setOnClickListener(listener);


        //Timer t = new Timer().schedule(run, 3);
    }

    private String run(String inputText)
    {
        //InputHandler input = new InputHandler("C2H6 + O2 -> CO2 + H2O");
        InputHandler input = new InputHandler(inputText);
        //return "hi";
        return input.getSolution();
        //R.layout.main_text
    }
}
