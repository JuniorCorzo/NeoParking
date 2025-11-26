package dev.angelcorzo.neoparking.jpa.rates.specialpolicies;

import dev.angelcorzo.neoparking.jpa.tenants.TenantsData;
import dev.angelcorzo.neoparking.model.specialpolicies.enums.ModifiesTypes;
import dev.angelcorzo.neoparking.model.specialpolicies.enums.OperationsTypes;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table()
@Entity(name = "special_policies")
@Check(
    name = "valid_percentage",
    constraints = "operation = 'PERCENTAGE' AND (value_to_modify > 0 AND value_to_modify <= 100)")
@SQLRestriction(value = "active IS TRUE")
public class SpecialPoliciesData {
  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
  private TenantsData tenant;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "modifies", nullable = false)
  @Enumerated(EnumType.STRING)
  private ModifiesTypes modifies;

  @Column(name = "operation")
  @Enumerated(EnumType.STRING)
  private OperationsTypes operation;

  @Column(name = "value_to_modify")
  private BigDecimal valueToModify;

  @Column(name = "active")
  @ColumnDefault(value = "TRUE")
  private boolean active;
}
