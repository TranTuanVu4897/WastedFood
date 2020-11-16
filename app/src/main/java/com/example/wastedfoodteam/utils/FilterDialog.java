package com.example.wastedfoodteam.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;

import java.sql.Time;

public class FilterDialog {
    private LayoutInflater inflater;
    private Context context;

    private Spinner spDistance;
    private Spinner spSellTime;
    private Spinner spDiscount;
    private Button btnClear;
    private Button btnConfirm;

    public FilterDialog(LayoutInflater inflater, Context context) {
        this.inflater = inflater;
        this.context = context;
    }

    public interface ModifyFilter {
        void onClear();
        void onChange();
    }


    public void showFilterDialog(final ModifyFilter modifyFilter) {
        View filterLayout = inflater.inflate(R.layout.dialog_buyer_filter, null);

        spDistance = filterLayout.findViewById(R.id.spDistance);
        spSellTime = filterLayout.findViewById(R.id.spSellTime);
        spDiscount = filterLayout.findViewById(R.id.spDiscount);
        btnClear = filterLayout.findViewById(R.id.btnClear);
        btnConfirm = filterLayout.findViewById(R.id.btnConfirm);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyFilter.onClear();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyFilter.onChange();
            }
        });

        setSpinnerDistance();
        setSpinnerSellTime();
        setSpinnerDiscount();

        AlertDialog.Builder builderDialogRating = new AlertDialog.Builder(context);
        builderDialogRating.setTitle("Tìm kiếm tiếp theo");
        builderDialogRating.setView(filterLayout);
        builderDialogRating.setCancelable(true);

        builderDialogRating.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialogRating = builderDialogRating.create();
        dialogRating.show();
    }

    private void setSpinnerDistance() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.direction_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistance.setAdapter(adapter);
        spDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Variable.distance = parent.getAdapter().getItem(position).toString().replace("km", "");//Get from string array
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerSellTime() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.sell_time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSellTime.setAdapter(adapter);
        spSellTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();
                if (item.equalsIgnoreCase("tất cả")) {
                    Variable.endTime = null;
                    Variable.startTime = null;
                }else{
                    Variable.startTime =  Time.valueOf(item.split("-")[0].replace("h","").trim() + ":00:00");
                    Variable.endTime =  Time.valueOf(item.split("-")[1].replace("h","").trim() + ":00:00");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerDiscount() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.discount_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDiscount.setAdapter(adapter);
        spDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();
                if (item.contains("%"))
                    Variable.discount = item.replace("%", "");
                else
                    Variable.discount = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
