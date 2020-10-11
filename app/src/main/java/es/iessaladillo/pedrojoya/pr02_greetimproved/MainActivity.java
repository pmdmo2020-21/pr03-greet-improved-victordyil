package es.iessaladillo.pedrojoya.pr02_greetimproved;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import es.iessaladillo.pedrojoya.pr02_greetimproved.databinding.MainActivityBinding;

import static es.iessaladillo.pedrojoya.pr02_greetimproved.R.color.colorAccent;

public class MainActivity extends AppCompatActivity {
    private MainActivityBinding binding;
    private int quantity;
    final int maxChar = 20;

    // TODO: 11/10/2020 Limpiar Codigo     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViews();
    }

    private void setupViews() {
        setValue();
        binding.mrRB.setChecked(true);
        binding.swPremium.setOnCheckedChangeListener((buttonView, isChecked) -> checkPremium(isChecked));
        binding.BTNGreet.setOnClickListener(v -> saludar());
        binding.RGGender.setOnCheckedChangeListener((group, checkedId) -> changeImage(checkedId));
        binding.GenderImg.getBaseline();
        binding.nameCharCounter.setText(getResources().getQuantityString(R.plurals.charCount, maxChar, maxChar));
        binding.subnameCounter.setText(getResources().getQuantityString(R.plurals.charCount, maxChar, maxChar));
        binding.subnameET.setOnEditorActionListener((textView, i, keyEvent) -> {
            saludar();
            return true;
        });
        focusChanger();
        comprobadorTexto();
    }

    void saludar() {
        esconderTeclado(binding.BTNGreet);
        if (binding.nameET.getText().length() == 0)
            isValid(binding.nameET, false);
        else if (binding.subnameET.getText().length() == 0)
            isValid(binding.subnameET, false);
        Toast toast;
        if (binding.nameET.length() > 0 && binding.subnameET.length() > 0) {
            if (quantity != 10 || binding.swPremium.isChecked()) {
                if (binding.CBPolitely.isChecked()) {
                    switch (binding.RGGender.getCheckedRadioButtonId()) {
                        case R.id.mrRB:
                            toast = Toast.makeText(this, String.format(getString(R.string.señorEducado), binding.nameET.getText(), binding.subnameET.getText()), Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        case R.id.mrsRB:
                            toast = Toast.makeText(this, (String.format(getString(R.string.señoritaEducado), binding.nameET.getText(), binding.subnameET.getText())), Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        case R.id.msRB:
                            toast = Toast.makeText(this, String.format(getString(R.string.señoraEducado), binding.nameET.getText(), binding.subnameET.getText()), Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                    }
                } else {
                    switch (binding.RGGender.getCheckedRadioButtonId()) {
                        case R.id.mrRB:
                            toast = Toast.makeText(this, String.format(getString(R.string.mrSaludo), binding.nameET.getText(), binding.subnameET.getText()), Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        case R.id.mrsRB:
                            toast = Toast.makeText(this, String.format(getString(R.string.señoritaSaludo), binding.nameET.getText(), binding.subnameET.getText()), Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        case R.id.msRB:
                            toast = Toast.makeText(this, String.format(getString(R.string.señoraSaludo), binding.nameET.getText(), binding.subnameET.getText()), Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                    }
                }
                quantity++;
                setValue();
            } else {
                toast = Toast.makeText(this, getString(R.string.avisoPremium), Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    private void changeImage(int checkedId) {
        switch (checkedId) {
            case R.id.mrRB:
                binding.GenderImg.setImageResource(R.drawable.ic_mr);
                break;
            case R.id.mrsRB:
                binding.GenderImg.setImageResource(R.drawable.ic_mrs);

                break;
            case R.id.msRB:
                binding.GenderImg.setImageResource(R.drawable.ic_ms);
                break;
        }
    }

    private void checkPremium(boolean isChecked) {
        if (isChecked) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.contador.setVisibility(View.INVISIBLE);

        } else {
            quantity = 0;
            setValue();
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.contador.setVisibility(View.VISIBLE);

        }
    }

    private void setValue() {
        binding.progressBar.setProgress(quantity);
        binding.contador.setText(String.format(getString(R.string.number), quantity));
    }

    private void comprobadorTexto() {
        comprobarNombre();
        comprobarApellido(binding.subnameET, binding.subnameCounter);
    }

    private void comprobarApellido(EditText p, TextView p2) {
        p.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                p2.setText(getResources().getQuantityString(R.plurals.charCount, (maxChar - editable.length()), (maxChar - editable.length())));
            }
        });
    }

    private void comprobarNombre() {
        comprobarApellido(binding.nameET, binding.nameCharCounter);
    }

    private void focusChanger() {
        binding.nameET.setOnFocusChangeListener((view, b) -> {
            changeColor(binding.nameCharCounter, b);
            isValid(binding.nameET, b);
        });
        binding.subnameET.setOnFocusChangeListener((view, b) -> {
            changeColor(binding.subnameCounter, b);
            if (!(binding.nameET.getText().length() == 0)) isValid(binding.subnameET, b);
        });
    }

    private void changeColor(TextView textView, boolean hasFocus) {
        int colorResID = hasFocus ? colorAccent : R.color.textPrimary;
        textView.setTextColor(ContextCompat.getColor(this, colorResID));
    }

    private void isValid(EditText textView, boolean b) {
        if (!b && textView.getText().length() == 0) {
            textView.setError("Required");
        } else textView.setError(null);
    }

    private void esconderTeclado(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}