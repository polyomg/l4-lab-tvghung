package poly.edu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String uri;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AccessTime", nullable = false)
    private Date accessTime;

    public Log(String username, String uri, Date accessTime) {
        this.username = username;
        this.uri = uri;
        this.accessTime = accessTime;
    }
}
