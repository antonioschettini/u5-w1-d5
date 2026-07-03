package antonioschettini.u5_w1_d5.repositories;

import antonioschettini.u5_w1_d5.entities.Postazione;
import antonioschettini.u5_w1_d5.enums.TipoPostazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostazioneRepository extends JpaRepository<Postazione, Long> {

    //query per cercare tutte le postazione che corrispondono ad un tipo specifico e che si trovano nella città
    List<Postazione> findByTipoAndEdificioCitta(TipoPostazione tipo, String citta);
}
