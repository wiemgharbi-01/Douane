package com.example.douane;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

// Activite 3 : Le Verdict (ResultActivity)
// Hethi l'activite theltha elli taffichi el verdict el nihayi (approuve walla refuse)
public class ResultActivity extends AppCompatActivity {

    // Les elements mte3 l'interface graphique (IDs fihom initiales wg)
    private LinearLayout llRootResult;
    private ImageView ivIconeVerdict;
    private TextView tvTitreVerdict;
    private TextView tvMessageVerdict;
    private Button btnNouveauVoyageur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Nfa3lou edge to edge
        WindowCompat.enableEdgeToEdge(getWindow());
        // N7ottou el layout mte3 ResultActivity
        setContentView(R.layout.activity_result);

        // N3adlou el vertical padding m3a les insets mte3 el portable (status bar w navigation bar)
        View root = findViewById(R.id.ll_root_result_wg);
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
        llRootResult = findViewById(R.id.ll_root_result_wg);
        ivIconeVerdict = findViewById(R.id.iv_icone_verdict_wg);
        tvTitreVerdict = findViewById(R.id.tv_titre_verdict_wg);
        tvMessageVerdict = findViewById(R.id.tv_message_verdict_wg);
        btnNouveauVoyageur = findViewById(R.id.btn_nouveau_voyageur_wg);

        // Nrecuperiw el verdict w esm el voyageur mel Intent
        recupererVerdictEtAfficher();

        // Clic 3la bouton "Nouveau voyageur" :
        // "Un bouton 'Nouveau voyageur' permet de revenir à la MainActivity en fermant les activités précédentes"
        btnNouveauVoyageur.setOnClickListener(v -> recommencerNouveauVoyageur());
    }

    // Methode bech nrecuperiw el donnees w nbadlou el fond w el message selon le verdict
    private void recupererVerdictEtAfficher() {
        Intent intent = getIntent();
        boolean verdict = false;
        String nomVoyageur = "Voyageur";

        if (intent != null) {
            String matricule = MainActivity.MATRICULE_ETUDIANT;
            String cleVerdict = MainActivity.genererCle(matricule, MainActivity.CLE_VERDICT);
            String cleNom = MainActivity.genererCle(matricule, MainActivity.CLE_NOM);

            verdict = intent.getBooleanExtra(cleVerdict, false);
            String nomRecu = intent.getStringExtra(cleNom);
            if (nomRecu != null && !nomRecu.trim().isEmpty()) {
                nomVoyageur = nomRecu.trim();
            }
        }

        // Ken le verdict approuvé (true) -> fond vert w msg "Bon voyage [Nom]"
        if (verdict) {
            llRootResult.setBackgroundColor(ContextCompat.getColor(this, R.color.verdict_approuve_bg));
            ivIconeVerdict.setImageResource(R.drawable.ic_check_circle);
            tvTitreVerdict.setText(R.string.titre_verdict);
            String msg = getString(R.string.msg_approuve, nomVoyageur);
            tvMessageVerdict.setText(msg);
        } else {
            // Ken le verdict refusé (false) -> fond rouge w msg "[Nom] est en état d'arrestation temporelle"
            llRootResult.setBackgroundColor(ContextCompat.getColor(this, R.color.verdict_refuse_bg));
            ivIconeVerdict.setImageResource(R.drawable.ic_cancel_circle);
            tvTitreVerdict.setText(R.string.titre_verdict);
            String msg = getString(R.string.msg_refuse, nomVoyageur);
            tvMessageVerdict.setText(msg);
        }
    }

    // Methode bech narj3ou lel MainActivity w nsakrou les activites elli 9balha
    private void recommencerNouveauVoyageur() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        // CLEAR_TOP ysakker kol les activites elli faw9 el MainActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
