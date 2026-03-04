package dev.angelcorzo.neoparking.paymentprovider.dtos.response;

import dev.angelcorzo.neoparking.model.payments.valueobject.ProviderMetadata;
import dev.angelcorzo.neoparking.paymentprovider.utils.PaymentProviderDateTimeFormatters;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record PayLinkResponse(
    long id,
    String title,
    String description,
    String date,
    int state,
    String txtCode,
    long clientId,
    int onePayment,
    int quantity,
    int baseTax,
    String currency,
    String typeSell,
    String urlConfirmation,
    String urlResponse,
    int tax,
    int icoTax,
    int amount,
    String invoiceNumber,
    String routeQr,
    String routeLink,
    String expirationDate) {

  public ProviderMetadata toProviderMetadata(String providerName) {
    return ProviderMetadata.builder()
        .provider(providerName)
        .externalPaymentId(invoiceNumber())
        .checkoutSessionId(cleanTxtCode(txtCode()))
        .checkoutUrl(routeLink())
        .checkoutExpiresAt(
            LocalDateTime.parse(
                    expirationDate(),
                    PaymentProviderDateTimeFormatters.RESPONSE_EPAYCO_DATE_TIME_FORMAT)
                .atOffset(ZoneOffset.UTC))
        .rawResponse(this)
        .build();
  }

  private String cleanTxtCode(String txtCode) {
    if (txtCode == null) return null;
    return txtCode.replace("\\", "").replace("\"", "");
  }
}
