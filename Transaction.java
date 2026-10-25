
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private final String type;
    private final BigDecimal amount;
    private final String category;
    private final String remarks;
    private final LocalDateTime timestamp;

    public Transaction(String type, BigDecimal amount,
                       String category, String remarks) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.remarks = remarks;
        this.timestamp = LocalDateTime.now();
    }
    
public Transaction(String type, BigDecimal amount,
                   String category, String remarks,
                   LocalDateTime timestamp) {
    this.type = type;
    this.amount = amount;
    this.category = category;
    this.remarks = remarks;
    this.timestamp = timestamp;
}


    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp + " | " + type + " | ₹" + amount
                + " | " + category + " | " + remarks;
    }
}
