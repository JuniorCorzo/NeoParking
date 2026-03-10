package dev.angelcorzo.neoparking.paymentprovider.config.json;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.angelcorzo.neoparking.paymentprovider.dtos.response.EpaycoResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.jsontype.impl.TypeIdResolverBase;

public class SuccessTypeIdResolver extends TypeIdResolverBase {
  private JavaType superType;

  @Override
  public void init(JavaType bt) {
    this.superType = bt;
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String id) throws JacksonException {
    Class<?> resolvedType =
        "true".equals(id) ? EpaycoResponse.Success.class : EpaycoResponse.Failure.class;
    return context.constructSpecializedType(this.superType, resolvedType);
  }

  @Override
  public String idFromValue(DatabindContext ctxt, Object value) throws JacksonException {
    return this.idFromValueAndType(ctxt, value, value.getClass());
  }

  @Override
  public String idFromValueAndType(DatabindContext ctxt, Object value, Class<?> suggestedType)
      throws JacksonException {
    return ((EpaycoResponse<?>) value).success() ? "true" : "false";
  }

  @Override
  public String idFromBaseType(DatabindContext ctxt) {
    return "true";
  }

  @Override
  public JsonTypeInfo.Id getMechanism() {
    return JsonTypeInfo.Id.CUSTOM;
  }
}
