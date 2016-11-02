package lt.petabitas.kainoskalkuliatorius;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.math.BigDecimal;
import java.math.RoundingMode;



public class Kaina extends AppCompatActivity {

    public static final String TAG = "KAINA";
    private EditText etextKainaBePVM;
    private EditText etextPVM;
    private EditText etextKainaSuPVM;
    private EditText etextMarza;
    private EditText etextPardBePVM;
    private EditText etextPardSuPVM;
    private EditText etextName;
    private Spinner spinner;
    private ListView listView;
    private DBHelper dbHelper;

    private BigDecimal pvm = new BigDecimal("0.21");
    private BigDecimal hundred = new BigDecimal("100");
    private BigDecimal one = new BigDecimal("1");

    private int save = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        etextPVM = (EditText) findViewById(R.id.etextPVM);
        etextPardBePVM = (EditText) findViewById(R.id.etextPardBePVM);
        spinner = (Spinner) findViewById(R.id.spinner);
        etextName = (EditText) findViewById(R.id.etextName);

        prepareExtraKainaBePVM();
        prepareExtraKainaSuPVM();
        prepareExtraMarza();
        prepareExtraPardSuPVM();
        prepareButtonCreate();
        prepareButtonEdit();
        prepareButtonDelete();
        prepareListView();
    }

    private void prepareListView() {
        listView = (ListView) findViewById(R.id.listView);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(dbHelper.getFavList(), this);
        listView.setAdapter(favoriteAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteListItem item = (FavoriteListItem) parent.getAdapter().getItem(position);

                save = item.getId();
                etextKainaBePVM.setText(item.getBePVM());
                etextPardSuPVM.setText(item.getKaina());
                etextName.setText(item.getPavadinimas());

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Kaina.this, R.array.grupes, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                if (!item.getRusis().equals(null)) {
                    int spinnerPosition = adapter.getPosition(item.getRusis());
                    spinner.setSelection(spinnerPosition);

                    BigDecimal p6 = convertToBigDecimal(etextPardSuPVM.getText().toString());
                    BigDecimal t6 = convertToBigDecimal(etextKainaBePVM.getText().toString());
                    BigDecimal v6 = t6.add(t6.multiply(pvm));
                    t6 = t6.multiply(pvm);
                    t6 = t6.setScale(2, RoundingMode.CEILING);

                    etextPVM.setText(t6.toString());

                    v6 = v6.setScale(2, RoundingMode.CEILING);
                    etextKainaSuPVM.setText(v6.toString());

                    BigDecimal spvm6 = convertToBigDecimal(etextKainaSuPVM.getText().toString());

                    if (spvm6.signum() > 0) {
                        BigDecimal m6 = (hundred.multiply(p6.subtract(spvm6))).divide(spvm6, 2, RoundingMode.HALF_DOWN);
                        m6 = m6.setScale(2, RoundingMode.CEILING);
                        etextMarza.setText(m6.toString());

                    } else {
                        etextMarza.setText("0");
                    }
                    p6 = p6.divide(pvm.add(one), 2, RoundingMode.HALF_DOWN);
                    p6 = p6.setScale(2, RoundingMode.CEILING);
                    etextPardBePVM.setText(p6.toString());

                }
            }
        });
    }

    private BigDecimal convertToBigDecimal(String text) {
        BigDecimal bd = new BigDecimal(0);
        try {
            bd = new BigDecimal(text);
        } catch (NumberFormatException nfe) {
            Log.w(TAG, "Unable to convert to decimal: " + text);
        }
        return bd;
    }

    private void prepareButtonDelete() {
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                dbHelper.removeFav(save);
                etextMarza.setText("");
                etextPardBePVM.setText("");
                etextPardSuPVM.setText("");
                etextKainaBePVM.setText("");
                etextPVM.setText("");
                etextKainaSuPVM.setText("");
                etextName.setText("");
                spinner.setSelection(0);

                Toast.makeText(Kaina.this, "Įrašas ištrintas", Toast.LENGTH_SHORT).show();
                updateAdapter();

            }
        });
    }

    private void prepareButtonEdit() {
        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String pavadinimas5 = etextName.getText().toString();
                String rusis5 = spinner.getSelectedItem().toString();
                BigDecimal t5 = new BigDecimal(0);
                try {
                    t5 = new BigDecimal(etextKainaBePVM.getText().toString());
                } catch (NumberFormatException nfe) {
                }
                String ts5 = String.valueOf(t5.doubleValue());

                BigDecimal kaina5 = new BigDecimal(0);
                try {
                    kaina5 = new BigDecimal(etextPardSuPVM.getText().toString());
                } catch (NumberFormatException nfe) {
                }
                String kainai5 = String.valueOf(kaina5.doubleValue());
                dbHelper.updatedata(save, pavadinimas5, rusis5, ts5, kainai5);

                Toast.makeText(Kaina.this, "Įrašas atnaujintas", Toast.LENGTH_SHORT).show();
                updateAdapter();

            }
        });
    }

    private void prepareButtonCreate() {
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String pavadinimas4 = etextName.getText().toString();
                String rusis4 = spinner.getSelectedItem().toString();
                BigDecimal t4 = new BigDecimal(0);
                try {
                    t4 = new BigDecimal(etextKainaBePVM.getText().toString());
                } catch (NumberFormatException nfe) {
                }
                String ts4 = String.valueOf(t4.doubleValue());

                BigDecimal kaina4 = new BigDecimal(0);
                try {
                    kaina4 = new BigDecimal(etextPardSuPVM.getText().toString());
                } catch (NumberFormatException nfe) {
                }
                String kainai4 = String.valueOf(kaina4.doubleValue());
                dbHelper.adddata(pavadinimas4, rusis4, ts4, kainai4);

                Toast.makeText(Kaina.this, "Įrašas sukurtas", Toast.LENGTH_SHORT).show();
                updateAdapter();
            }

        });
    }

    private void prepareExtraPardSuPVM() {
        etextPardSuPVM = (EditText) findViewById(R.id.etextPardSuPVM);
        etextPardSuPVM.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (etextPardSuPVM.isFocused()) {
                    if (s.length() != 0) {
                        BigDecimal p3 = new BigDecimal(0);
                        try {
                            p3 = new BigDecimal(etextPardSuPVM.getText().toString());
                        } catch (NumberFormatException nfe) {
                        }
                        BigDecimal spvm3 = new BigDecimal(0);
                        try {
                            spvm3 = new BigDecimal(etextKainaSuPVM.getText().toString());
                        } catch (NumberFormatException nfe) {
                        }
                        BigDecimal m3 = new BigDecimal(0);
                        if (spvm3.signum() > 0) {
                            m3 = (hundred.multiply(p3.subtract(spvm3))).divide(spvm3, 2, RoundingMode.HALF_DOWN);
                            m3 = m3.setScale(2, RoundingMode.CEILING);
                            etextMarza.setText(m3.toString());
                        } else etextMarza.setText("0");

                        p3 = p3.divide(pvm.add(one), 2, RoundingMode.HALF_DOWN);
                        p3 = p3.setScale(2, RoundingMode.CEILING);
                        etextPardBePVM.setText(p3.toString());

                    } else {
                        etextMarza.setText("0");
                        etextPardBePVM.setText("0");
                    }
                }
            }
        });
    }

    private void prepareExtraMarza() {
        etextMarza = (EditText) findViewById(R.id.etextMarza);
        etextMarza.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (etextMarza.isFocused()) {
                    if (s.length() != 0) {
                        BigDecimal m2 = convertToBigDecimal(etextMarza.getText().toString());
                        BigDecimal bpvm2 = convertToBigDecimal(etextKainaBePVM.getText().toString());

                        m2 = m2.divide(hundred, 2, RoundingMode.HALF_DOWN);
                        m2 = (bpvm2.multiply(m2)).add(bpvm2);
                        m2 = m2.setScale(2, RoundingMode.CEILING);
                        etextPardBePVM.setText(m2.toString());

                        BigDecimal ps2 = m2.add(m2.multiply(pvm));
                        ps2 = ps2.setScale(2, RoundingMode.CEILING);
                        etextPardSuPVM.setText(ps2.toString());
                    }
                }
            }
        });
        etextMarza.setText("0");
    }

    private void prepareExtraKainaSuPVM() {
        etextKainaSuPVM = (EditText) findViewById(R.id.etextKainaSuPVM);
        etextKainaSuPVM.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (etextKainaSuPVM.isFocused()) {
                    if (s.length() != 0) {
                        BigDecimal spvm1 = convertToBigDecimal(etextKainaSuPVM.getText().toString());
                        spvm1 = spvm1.divide(pvm.add(one), 2, RoundingMode.HALF_DOWN);
                        BigDecimal pbm1 = spvm1;
                        spvm1 = spvm1.setScale(2, RoundingMode.CEILING);
                        etextKainaBePVM.setText(spvm1.toString());

                        spvm1 = spvm1.multiply(pvm);
                        spvm1 = spvm1.setScale(2, RoundingMode.CEILING);
                        etextPVM.setText(spvm1.toString());

                        BigDecimal m1 = convertToBigDecimal(etextMarza.getText().toString());
                        m1 = m1.divide(hundred, 2, RoundingMode.HALF_DOWN);
                        m1 = (pbm1.multiply(m1)).add(pbm1);
                        m1 = m1.setScale(2, RoundingMode.CEILING);
                        etextPardBePVM.setText(m1.toString());

                        BigDecimal ps1 = m1.add(m1.multiply(pvm));
                        ps1 = ps1.setScale(2, RoundingMode.CEILING);
                        etextPardSuPVM.setText(ps1.toString());

                    } else {
                        etextPVM.setText("0");
                        etextKainaBePVM.setText("0");
                        etextPardBePVM.setText("0");
                        etextPardSuPVM.setText("0");
                    }
                }
            }

        });
    }

    private void prepareExtraKainaBePVM() {
        etextKainaBePVM = (EditText) findViewById(R.id.etextKainaBePVM);
        etextKainaBePVM.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (etextKainaBePVM.isFocused()) {
                    if (s.length() != 0) {
                        BigDecimal t = new BigDecimal(0);
                        try {
                            t = new BigDecimal(etextKainaBePVM.getText().toString());
                        } catch (NumberFormatException nfe) {
                        }
                        BigDecimal m = new BigDecimal(0);
                        try {
                            m = new BigDecimal(etextMarza.getText().toString());
                        } catch (NumberFormatException nfe) {
                        }
                        m = m.divide(hundred, 2, RoundingMode.HALF_DOWN);
                        m = (t.multiply(m)).add(t);
                        BigDecimal ps = m;
                        m = m.setScale(2, RoundingMode.CEILING);
                        etextPardBePVM.setText(m.toString());

                        ps = m.add(m.multiply(pvm));
                        ps = ps.setScale(2, RoundingMode.CEILING);
                        etextPardSuPVM.setText(ps.toString());

                        BigDecimal v = t.add(t.multiply(pvm));
                        t = t.multiply(pvm);
                        t = t.setScale(2, RoundingMode.CEILING);
                        v = v.setScale(2, RoundingMode.CEILING);
                        etextPVM.setText(t.toString());
                        etextKainaSuPVM.setText(v.toString());

                    } else {
                        etextPVM.setText("0");
                        etextKainaSuPVM.setText("0");
                        etextPardBePVM.setText("0");
                        etextPardSuPVM.setText("0");
                    }
                }
            }
        });
    }

    private void updateAdapter() {
        FavoriteAdapter favoriteAdapter = (FavoriteAdapter) listView.getAdapter();
        favoriteAdapter.setFavorites(dbHelper.getFavList());
        favoriteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kaina, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}





