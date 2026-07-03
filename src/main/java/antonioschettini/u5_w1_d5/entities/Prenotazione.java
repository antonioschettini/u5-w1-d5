package antonioschettini.u5_w1_d5.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni")
@Data
@NoArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "data_validità")
    private LocalDate dataValidita;

    // relazione tante prenotazioni possono essere fatte dallo stesso Utente
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    // relazione tante prenotazioni posso essere nella stessa postazione
    @ManyToOne
    @JoinColumn(name = "postazione_id")
    private Postazione postazione;

    public Prenotazione(LocalDate dataValidita, Utente utente, Postazione postazione) {
        this.dataValidita = dataValidita;
        this.utente = utente;
        this.postazione = postazione;
    }
}
