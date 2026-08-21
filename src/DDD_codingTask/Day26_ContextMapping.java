// Task 19 (Coding 1) - Context Mapping between Billing and Shipping
//
// Context map used here:
//
//     Billing  ---( Customer / Supplier )--->  Shipping
//     upstream                                 downstream
//
// Billing is upstream: once an invoice is paid it publishes a fact.
// Shipping is downstream: it reacts and creates a shipment.
//
// The integration point is an INTERFACE owned by the downstream side
// (ShipmentGateway), so Billing depends on an abstraction and never on
// Shipping's internal model. Each context keeps its own vocabulary:
//     Billing speaks   -> invoice, amount, payer, paid
//     Shipping speaks  -> consignment, weight, address, dispatched

import java.util.ArrayList;
import java.util.List;

/* ==================== BILLING CONTEXT ==================== */

class Invoice {

    private final String invoiceNumber;
    private final String payerName;
    private final double amount;
    private boolean paid;

    Invoice(String invoiceNumber, String payerName, double amount) {
        this.invoiceNumber = invoiceNumber;
        this.payerName = payerName;
        this.amount = amount;
        this.paid = false;
    }

    String getInvoiceNumber() {
        return invoiceNumber;
    }

    String getPayerName() {
        return payerName;
    }

    double getAmount() {
        return amount;
    }

    boolean isPaid() {
        return paid;
    }

    void markPaid() {
        this.paid = true;
    }
}

// The published contract of Billing (its "published language").
// Deliberately a small, flat message - not the Invoice entity itself.
class InvoicePaidEvent {

    private final String invoiceNumber;
    private final String payerName;
    private final double amountPaid;

    InvoicePaidEvent(String invoiceNumber, String payerName, double amountPaid) {
        this.invoiceNumber = invoiceNumber;
        this.payerName = payerName;
        this.amountPaid = amountPaid;
    }

    String getInvoiceNumber() {
        return invoiceNumber;
    }

    String getPayerName() {
        return payerName;
    }

    double getAmountPaid() {
        return amountPaid;
    }
}

// Integration point (port). Billing knows only this interface.
interface ShipmentGateway {

    String requestShipment(InvoicePaidEvent event, String deliveryAddress);
}

class BillingService {

    private final ShipmentGateway shipmentGateway;
    private final List<Invoice> invoices = new ArrayList<>();

    BillingService(ShipmentGateway shipmentGateway) {
        this.shipmentGateway = shipmentGateway;
    }

    Invoice createInvoice(String number, String payer, double amount) {

        Invoice invoice = new Invoice(number, payer, amount);
        invoices.add(invoice);
        return invoice;
    }

    // Billing's own use case. It ends by publishing a fact.
    String payInvoice(String invoiceNumber, String deliveryAddress) {

        Invoice invoice = invoices.stream()
                .filter(i -> i.getInvoiceNumber().equals(invoiceNumber))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invoice not found"));

        if (invoice.isPaid()) {
            throw new IllegalStateException("Invoice already paid");
        }

        invoice.markPaid();
        System.out.println("[Billing]  Invoice " + invoiceNumber +
                " paid, amount " + invoice.getAmount());

        InvoicePaidEvent event = new InvoicePaidEvent(
                invoice.getInvoiceNumber(),
                invoice.getPayerName(),
                invoice.getAmount()
        );

        // crossing the context boundary through the interface only
        return shipmentGateway.requestShipment(event, deliveryAddress);
    }
}

/* ==================== SHIPPING CONTEXT ==================== */

class Consignment {

    private final String consignmentId;
    private final String receiverName;
    private final String address;
    private String status;

    Consignment(String consignmentId, String receiverName, String address) {
        this.consignmentId = consignmentId;
        this.receiverName = receiverName;
        this.address = address;
        this.status = "CREATED";
    }

    String getConsignmentId() {
        return consignmentId;
    }

    void dispatch() {
        this.status = "DISPATCHED";
    }

    @Override
    public String toString() {
        return "Consignment{id=" + consignmentId +
                ", receiver=" + receiverName +
                ", address=" + address +
                ", status=" + status + "}";
    }
}

class ShippingService {

    private final List<Consignment> consignments = new ArrayList<>();
    private int counter;

    // Shipping's own language - it has no idea what an "invoice" is
    Consignment createConsignment(String receiverName, String address) {

        counter++;

        Consignment consignment = new Consignment(
                "CN-" + counter, receiverName, address
        );

        consignments.add(consignment);

        System.out.println("[Shipping] Created " + consignment);

        return consignment;
    }

    void dispatch(String consignmentId) {

        consignments.stream()
                .filter(c -> c.getConsignmentId().equals(consignmentId))
                .findFirst()
                .ifPresent(c -> {
                    c.dispatch();
                    System.out.println("[Shipping] Dispatched " + c);
                });
    }
}

// The adapter that implements Billing's port using Shipping's model.
// This is where the translation between the two languages happens.
class ShippingGatewayAdapter implements ShipmentGateway {

    private final ShippingService shippingService;

    ShippingGatewayAdapter(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @Override
    public String requestShipment(InvoicePaidEvent event, String deliveryAddress) {

        System.out.println("[Adapter]  Translating invoice " +
                event.getInvoiceNumber() + " into a consignment");

        // payer (Billing word) -> receiver (Shipping word)
        Consignment consignment = shippingService.createConsignment(
                event.getPayerName(), deliveryAddress
        );

        return consignment.getConsignmentId();
    }
}

public class Day26_ContextMapping {

    public static void main(String[] args) {

        ShippingService shippingService = new ShippingService();
        ShipmentGateway gateway = new ShippingGatewayAdapter(shippingService);
        BillingService billingService = new BillingService(gateway);

        billingService.createInvoice("INV-501", "Harsh Saini", 12500.00);

        String consignmentId = billingService.payInvoice(
                "INV-501", "Sector 22, Chandigarh"
        );

        shippingService.dispatch(consignmentId);

        System.out.println("\nBoundary respected:");
        System.out.println(" - Billing never imports Consignment");
        System.out.println(" - Shipping never imports Invoice");
        System.out.println(" - Only InvoicePaidEvent crosses the boundary");
    }
}
