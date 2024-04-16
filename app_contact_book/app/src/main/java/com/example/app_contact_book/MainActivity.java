package com.example.app_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    EditText textName, textPhone, textEmail;
    SearchView searchView;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textName = findViewById(R.id.textName);
        textPhone = findViewById(R.id.textPhone);
        textEmail = findViewById(R.id.textEmail);
        buttonSave = findViewById(R.id.buttonSave);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContactOnClick();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void saveContactOnClick(View v) {
        String name = textName.getText().toString();
        String phone = textPhone.getText().toString();
        String email = textEmail.getText().toString();

        Log.d("SaveContact", "Guardando el siguiente contacto: ");
        Log.d("SaveContact", "Nombre: " + name);
        Log.d("SaveContact", "Teléfono: " + phone);
        Log.d("SaveContact", "Email: " + email);

        SharedPreferences preferences = getSharedPreferences("contacts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String contactsJson = preferences.getString("contacts_list", "[]");
        try {
            JSONArray contactsArray = new JSONArray(contactsJson);

            JSONObject newContact = new JSONObject();
            newContact.put("name", name);
            newContact.put("phone", phone);
            newContact.put("email", email);

            contactsArray.put(newContact);

            editor.putString("contacts_list", contactsArray.toString());
            editor.commit();

            Toast.makeText(this, "Datos guardados correctamente!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar los datos.", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchContactOnClick() {
        String searchText = searchView.getQuery().toString();

        SharedPreferences preferences = getSharedPreferences("contacts", Context.MODE_PRIVATE);
        String contactsJson = preferences.getString("contacts_list", "[]");

        try {
            JSONArray contactsArray = new JSONArray(contactsJson);
            boolean found = false;

            for (int i = 0; i < contactsArray.length(); i++) {
                JSONObject contact = contactsArray.getJSONObject(i);
                String name = contact.getString("name");
                String phone = contact.getString("phone");
                String email = contact.getString("email");

                if (name.contains(searchText) || phone.contains(searchText) || email.contains(searchText)) {
                    textName.setText(name);
                    textPhone.setText(phone);
                    textEmail.setText(email);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Toast.makeText(this, "Contacto no encontrado.", Toast.LENGTH_SHORT).show();
                textName.setText("");
                textPhone.setText("");
                textEmail.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al buscar el contacto.", Toast.LENGTH_SHORT).show();
        }
    }
}