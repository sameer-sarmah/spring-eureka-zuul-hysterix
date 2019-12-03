package delivery.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "delivery_agent", catalog = "delivery",schema= "delivery")
public class DeliveryAgent {
    @Id
    @Column(name = "agent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "agent_name")
    private String contactName;
    private String phone;

    private boolean available;

}
