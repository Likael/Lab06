package pollub.ism.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import pollub.ism.lab06.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayAdapter<CharSequence> adapter;
    MagazynBazaDanych bazaDanych;
    String wybraneWarzywoNazwa = null;
    Integer wybraneWarzywoIlosc = null;

    public enum OperacjaMagazynowa {SKLADUJ, WYDAJ};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = ArrayAdapter.createFromResource(this, R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);

        bazaDanych = new MagazynBazaDanych(this);


        binding.addToDatabase.setOnClickListener(view -> zmienStan(OperacjaMagazynowa.SKLADUJ));

        binding.removeFromDatabase.setOnClickListener(view -> zmienStan(OperacjaMagazynowa.WYDAJ));

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wybraneWarzywoNazwa = adapter.getItem(i).toString();
                aktualizuj();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nie będziemy implementować, ale musi być
            }
        });
    }

    private void aktualizuj(){

        wybraneWarzywoIlosc = bazaDanych.podajIlosc(wybraneWarzywoNazwa);
        binding.state.setText("Stan magazynu dla " + wybraneWarzywoNazwa + " wynosi: " + wybraneWarzywoIlosc);
    }

    private void zmienStan(OperacjaMagazynowa operacja){

        Integer zmianaIlosci = null, nowaIlosc = null;

        try {
            zmianaIlosci = Integer.parseInt(binding.qunatity.getText().toString());
        }catch(NumberFormatException ex){
            return;
        }finally {
            binding.qunatity.setText("");
        }

        switch (operacja){
            case SKLADUJ: nowaIlosc = wybraneWarzywoIlosc + zmianaIlosci; break;
            case WYDAJ: nowaIlosc = wybraneWarzywoIlosc - zmianaIlosci; break;
        }

        bazaDanych.zmienStanMagazynu(wybraneWarzywoNazwa, nowaIlosc);

        aktualizuj();

    }
}