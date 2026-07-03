package antonioschettini.u5_w1_d5.repositories;

import antonioschettini.u5_w1_d5.entities.Postazione;
import antonioschettini.u5_w1_d5.entities.Prenotazione;
import antonioschettini.u5_w1_d5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    // query per verificare se esiste già una prenotazione per quella postazione in quello specifico giorno
    boolean existsByPostazioneAndDataValidita(Postazione postazione, LocalDate localDate);

    //query per verificare se esiste già una prenotazione per quell utente in quel preciso giorno
    boolean existsByUtenteAndDataValidita(Utente utente, LocalDate localDate);
}
