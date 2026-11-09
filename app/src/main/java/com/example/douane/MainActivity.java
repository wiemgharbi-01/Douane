package com.example.douane;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

// Activite 1 : Le Formulaire d'Enregistrement (MainActivity)
// Hethi l'interface loula elli fiha formulaire bech nsajlou les informations mte3 l voyageur
public class MainActivity extends AppCompatActivity {

    // 4 ar9am lekhraniyin mte3 carte etudiant (matricule) kima matloub fel sujet
    // Ynejjem l etudiant ybaddel "1234" b les 4 derniers chiffres mte3ou
    public static final String MATRICULE_ETUDIANT = "1234";

    // Assemi les cles de base mte3 les donnees elli bech yet3addew fel Intent
    public static final String CLE_NOM = "NOM_VOYAGEUR";
    public static final String CLE_ANNEE_NAISSANCE = "ANNEE_NAISSANCE";
    public static final String CLE_ANNEE_DESTINATION = "ANNEE_DESTINATION";
    public static final String CLE_MOTIF = "MOTIF_VOYAGE";
    public static final String CLE_VERDICT = "VERDICT_FINAL";

    // Les elements mte3 l'interface graphique (IDs fihom initiales wg)
    private TextInputEditText etNom;
    private TextInputEditText etAnneeNaissance;
    private TextInputEditText etAnneeDestination;
    private RadioGroup rgMotif;
    private Button btnInspecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // N7ottou el layout mte3 el MainActivity
        setContentView(R.layout.activity_main);

        // Nrecuperiw les elements mel XML bel findViewById
        etNom = findViewById(R.id.et_nom_wg);
        etAnneeNaissance = findViewById(R.id.et_annee_naissance_wg);
        etAnneeDestination = findViewById(R.id.et_annee_destination_wg);
        rgMotif = findViewById(R.id.rg_motif_wg);
        btnInspecter = findViewById(R.id.btn_inspecter_wg);

        // Evenement au clic 3la bouton "Inspecter le profil"
        btnInspecter.setOnClickListener(v -> validerEtInspecterProfil());
    }

    // Methode bech nverifiw les donnees w nlanziw el Activite 2
    private void validerEtInspecterProfil() {
        // Nrecuperiw el texte elli ktebhou l utilisateur w na7iw les espaces zeydin
        String nom = etNom.getText() != null ? etNom.getText().toString().trim() : "";
        String strNaissance = etAnneeNaissance.getText() != null ? etAnneeNaissance.getText().toString().trim() : "";
        String strDestination = etAnneeDestination.getText() != null ? etAnneeDestination.getText().toString().trim() : "";

        // Verifi ken el nom feragh
        if (TextUtils.isEmpty(nom)) {
            etNom.setError(getString(R.string.err_nom_requis));
            etNom.requestFocus();
            return;
        }

        // Verifi ken annee de naissance fergha
        if (TextUtils.isEmpty(strNaissance)) {
            etAnneeNaissance.setError(getString(R.string.err_naissance_requise));
            etAnneeNaissance.requestFocus();
            return;
        }

        // Verifi ken annee de destination fergha
        if (TextUtils.isEmpty(strDestination)) {
            etAnneeDestination.setError(getString(R.string.err_destination_requise));
            etAnneeDestination.requestFocus();
            return;
        }

        // N7awlou les annees l entier (int) w nverifiw el validite mte3hom
        int anneeNaissance;
        int anneeDestination;
        try {
            anneeNaissance = Integer.parseInt(strNaissance);
            anneeDestination = Integer.parseInt(strDestination);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.err_annee_invalide, Toast.LENGTH_SHORT).show();
            return;
        }

        // Nchoufou anehou bouton radio li msélectionné
        int selectedRadioId = rgMotif.getCheckedRadioButtonId();
        RadioButton rbSelected = findViewById(selectedRadioId);
        String motif = (rbSelected != null) ? rbSelected.getText().toString() : getString(R.string.motif_tourisme);

        // Nasn3ou Intent explicite bech nemchiw lel Activite 2 (VerificationActivity)
        Intent intent = new Intent(MainActivity.this, VerificationActivity.class);

        // N3abiw el Intent bel les donnees en utilisant la methode statique m3a matricule
        intent.putExtra(genererCle(MATRICULE_ETUDIANT, CLE_NOM), nom);
        intent.putExtra(genererCle(MATRICULE_ETUDIANT, CLE_ANNEE_NAISSANCE), anneeNaissance);
        intent.putExtra(genererCle(MATRICULE_ETUDIANT, CLE_ANNEE_DESTINATION), anneeDestination);
        intent.putExtra(genererCle(MATRICULE_ETUDIANT, CLE_MOTIF), motif);

        // Nlanziw el activite 2 bel startActivity
        startActivity(intent);
    }

    /**
     * Methode statique kima matloub fel sujet :
     * "Toutes les clés utilisées dans les putExtra doivent être préfixées par les 4 derniers
     * chiffres de la carte d'étudiant d'un des membres. Ces clés ne doivent pas être codées en dur,
     * mais construites via une méthode statique qui prend le matricule en paramètre."
     *
     * @param matricule Les 4 derniers chiffres mte3 carte etudiant
     * @param nomCle Esm el cle de base (ex: NOM_VOYAGEUR)
     * @return El cle kamla prefexee bel matricule (ex: 1234_NOM_VOYAGEUR)
     */
    public static String genererCle(String matricule, String nomCle) {
        return matricule + "_" + nomCle;
    }
}
