package model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
public class Receipt {
    private long buyerId;
    private Set<ReceiptLine> lines;

    public Receipt(long buyerId) {
        this.buyerId = buyerId;
        this.lines = new LinkedHashSet<>();
    }

    public void addLine(ReceiptLine line) {
        this.lines.add(line);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Receipt {\n");
        sb.append("  buyerId: ").append(buyerId).append(",\n");
        sb.append("  lines: [\n");
        if (lines != null && !lines.isEmpty()) {
            for (ReceiptLine line : lines) {
                sb.append("    ").append(line).append(",\n");
            }
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }
}
