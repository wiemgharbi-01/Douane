package com.example.douane;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

// Activite 2 : Le Scanner de Paradoxe (VerificationActivity)
// Hethi l'activite theneya elli tverifi el profil w tscani les paradoxes temporels
public class VerificationActivity extends AppCompatActivity {

    // Les elements mte3 l'interface graphique (IDs fihom initiales wg)
    private ImageView ivPhotoPasseport;
    private TextView tvNomValeur;
    private TextView tvAgeValeur;
    private TextView tvMotifValeur;
    private TextView tvAlerteParadoxe;
    private Button btnApprouver;
    private Button btnRefuser;

    // Les donnees recues mel MainActivity
    private String nomVoyageur = "";
    private int anneeNaissance = 0;
    private int anneeDestination = 0;
    private String motifVoyage = "";
    private String photoUriString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nfa3lou edge to edge
        WindowCompat.enableEdgeToEdge(getWindow());
        // N7ottou el layout mte3 el VerificationActivity (passeport / billet)
        setContentView(R.layout.activity_verification);

        // N3adlou el vertical padding m3a les insets mte3 el portable (status bar w navigation bar)
        View root = findViewById(R.id.ll_root_verification_wg);
        int initialPaddingTop = root.getPaddingTop();
        int initialPaddingBottom = root.getPaddingBottom();
        int initialPaddingLeft = root.getPaddingLeft();
        int initialPaddingRight = root.getPaddingRight();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                initialPaddingLeft + systemBars.left,
                initialPaddingTop + systemBars.top,
                initialPaddingRight + systemBars.right,
                initialPaddingBottom + systemBars.bottom
            );
            return insets;
        });

        // Nrecuperiw les elements mel XML bel findViewById
        ivPhotoPasseport = findViewById(R.id.iv_photo_passeport_wg);
        tvNomValeur = findViewById(R.id.tv_nom_valeur_wg);
        tvAgeValeur = findViewById(R.id.tv_age_valeur_wg);
        tvMotifValeur = findViewById(R.id.tv_motif_valeur_wg);
        tvAlerteParadoxe = findViewById(R.id.tv_alerte_paradoxe_wg);
        btnApprouver = findViewById(R.id.btn_approuver_wg);
        btnRefuser = findViewById(R.id.btn_refuser_wg);

        // Nrecuperiw les extras mel Intent en utilisant la methode statique m3a matricule
        recupererDonneesIntent();

        // Naffichiw el photo mte3 el voyageur ken moujouda fel extras
        if (photoUriString != null && !photoUriString.trim().isEmpty()) {
            try {
                Uri uri = Uri.parse(photoUriString);
                ivPhotoPasseport.setImageURI(uri);
            } catch (Exception e) {
                ivPhotoPasseport.setImageResource(R.drawable.ic_passport_avatar);
            }
        }

        // N7sbou l age temporel calculé (destination - naissance)
        int ageTemporel = anneeDestination - anneeNaissance;
        int differenceAnnees = Math.abs(ageTemporel);

        // Naffichiw les donnees fi l'interface mte3 el passeport
        tvNomValeur.setText(nomVoyageur);
        String texteAge = ageTemporel + " ans (vers l'an " + anneeDestination + ")";
        tvAgeValeur.setText(texteAge);
        tvMotifValeur.setText(motifVoyage);

        // Verification mte3 el contrainte speciale :
        // "Si le motif du voyage est 'Modification du passé' ET que la différence entre
        // l'année de destination et l'année de naissance est un nombre premier, le bouton
        // 'Approuver' de l'Activité 2 doit être désactivé (disabled) dès le chargement"
        boolean isModificationPasse = motifVoyage != null && motifVoyage.trim().equalsIgnoreCase("Modification du passé");
        boolean isDiffPremier = estNombrePremier(differenceAnnees);

        if (isModificationPasse && isDiffPremier) {
            // Ndesactiviw el bouton approuver direct mchargement mte3 l activite
            btnApprouver.setEnabled(false);
            btnApprouver.setAlpha(0.4f);

            // Naffichiw msg d alerte rouge 3la khatar famma paradoxe
            tvAlerteParadoxe.setVisibility(View.VISIBLE);
            String msgAlerte = "ALERTE PARADOXE : Motif 'Modification du passé' w el fer9 bin l'annee ("
                    + differenceAnnees + ") howa 3dad premier ! El bouton approuver tsakker.";
            tvAlerteParadoxe.setText(msgAlerte);
        } else {
            tvAlerteParadoxe.setVisibility(View.GONE);
        }

        // Clic 3la bouton "Approuver le saut temporel" -> verdict = true
        btnApprouver.setOnClickListener(v -> lancerResultActivity(true));

        // Clic 3la bouton "Refuser (Risque de paradoxe)" -> verdict = false
        btnRefuser.setOnClickListener(v -> lancerResultActivity(false));
    }

    // Methode bech nrecuperiw les extras bel methode statique
    private void recupererDonneesIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String matricule = MainActivity.MATRICULE_ETUDIANT;
            String cleNom = MainActivity.genererCle(matricule, MainActivity.CLE_NOM);
            String cleNaissance = MainActivity.genererCle(matricule, MainActivity.CLE_ANNEE_NAISSANCE);
            String cleDestination = MainActivity.genererCle(matricule, MainActivity.CLE_ANNEE_DESTINATION);
            String cleMotif = MainActivity.genererCle(matricule, MainActivity.CLE_MOTIF);
            String cleImage = MainActivity.genererCle(matricule, MainActivity.CLE_IMAGE_URI);

            nomVoyageur = intent.getStringExtra(cleNom);
            if (nomVoyageur == null) nomVoyageur = "";
            anneeNaissance = intent.getIntExtra(cleNaissance, 0);
            anneeDestination = intent.getIntExtra(cleDestination, 0);
            motifVoyage = intent.getStringExtra(cleMotif);
            if (motifVoyage == null) motifVoyage = "";
            photoUriString = intent.getStringExtra(cleImage);
        }
    }

    // Methode bech nlanziw ResultActivity bel Intent explicite w n3adiw le verdict w le nom
    private void lancerResultActivity(boolean verdict) {
        Intent intent = new Intent(VerificationActivity.this, ResultActivity.class);

        // N3adiw le verdict (booléen) w le nom du voyageur bel cles prefexees
        String matricule = MainActivity.MATRICULE_ETUDIANT;
        intent.putExtra(MainActivity.genererCle(matricule, MainActivity.CLE_VERDICT), verdict);
        intent.putExtra(MainActivity.genererCle(matricule, MainActivity.CLE_NOM), nomVoyageur);

        // Ndemarriw l activite 3
        startActivity(intent);
    }

    /**
     * Methode statique bech nchoufou ken el 3dad premier (prime number) walla le
     * 3dad premier : akber men 1 w maye9bel el 9asma ken 3la 1 w 3la rou7ou
     *
     * @param n El 3dad li nverifiw fih
     * @return true kenou premier, sinon false
     */
    public static boolean estNombrePremier(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
