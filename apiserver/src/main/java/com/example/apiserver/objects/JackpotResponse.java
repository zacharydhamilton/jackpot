/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.example.apiserver.objects;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class JackpotResponse extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2037761452047091314L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"JackpotResponse\",\"namespace\":\"com.example.apiserver.objects\",\"fields\":[{\"name\":\"jackpotPoolId\",\"type\":\"string\",\"doc\":\"The ID of a specific Jackpot pool\"},{\"name\":\"region\",\"type\":\"string\",\"doc\":\"The geographic region of the Jackpot pool\"},{\"name\":\"jackpot\",\"type\":\"int\",\"doc\":\"The current winnable Jackpot\"},{\"name\":\"status\",\"type\":\"string\",\"doc\":\"The status of the request for a Jackpot\"}],\"version\":\"1\"}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<JackpotResponse> ENCODER =
      new BinaryMessageEncoder<JackpotResponse>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<JackpotResponse> DECODER =
      new BinaryMessageDecoder<JackpotResponse>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<JackpotResponse> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<JackpotResponse> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<JackpotResponse> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<JackpotResponse>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this JackpotResponse to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a JackpotResponse from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a JackpotResponse instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static JackpotResponse fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** The ID of a specific Jackpot pool */
  private java.lang.CharSequence jackpotPoolId;
  /** The geographic region of the Jackpot pool */
  private java.lang.CharSequence region;
  /** The current winnable Jackpot */
  private int jackpot;
  /** The status of the request for a Jackpot */
  private java.lang.CharSequence status;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public JackpotResponse() {}

  /**
   * All-args constructor.
   * @param jackpotPoolId The ID of a specific Jackpot pool
   * @param region The geographic region of the Jackpot pool
   * @param jackpot The current winnable Jackpot
   * @param status The status of the request for a Jackpot
   */
  public JackpotResponse(java.lang.CharSequence jackpotPoolId, java.lang.CharSequence region, java.lang.Integer jackpot, java.lang.CharSequence status) {
    this.jackpotPoolId = jackpotPoolId;
    this.region = region;
    this.jackpot = jackpot;
    this.status = status;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return jackpotPoolId;
    case 1: return region;
    case 2: return jackpot;
    case 3: return status;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: jackpotPoolId = (java.lang.CharSequence)value$; break;
    case 1: region = (java.lang.CharSequence)value$; break;
    case 2: jackpot = (java.lang.Integer)value$; break;
    case 3: status = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'jackpotPoolId' field.
   * @return The ID of a specific Jackpot pool
   */
  public java.lang.CharSequence getJackpotPoolId() {
    return jackpotPoolId;
  }


  /**
   * Sets the value of the 'jackpotPoolId' field.
   * The ID of a specific Jackpot pool
   * @param value the value to set.
   */
  public void setJackpotPoolId(java.lang.CharSequence value) {
    this.jackpotPoolId = value;
  }

  /**
   * Gets the value of the 'region' field.
   * @return The geographic region of the Jackpot pool
   */
  public java.lang.CharSequence getRegion() {
    return region;
  }


  /**
   * Sets the value of the 'region' field.
   * The geographic region of the Jackpot pool
   * @param value the value to set.
   */
  public void setRegion(java.lang.CharSequence value) {
    this.region = value;
  }

  /**
   * Gets the value of the 'jackpot' field.
   * @return The current winnable Jackpot
   */
  public int getJackpot() {
    return jackpot;
  }


  /**
   * Sets the value of the 'jackpot' field.
   * The current winnable Jackpot
   * @param value the value to set.
   */
  public void setJackpot(int value) {
    this.jackpot = value;
  }

  /**
   * Gets the value of the 'status' field.
   * @return The status of the request for a Jackpot
   */
  public java.lang.CharSequence getStatus() {
    return status;
  }


  /**
   * Sets the value of the 'status' field.
   * The status of the request for a Jackpot
   * @param value the value to set.
   */
  public void setStatus(java.lang.CharSequence value) {
    this.status = value;
  }

  /**
   * Creates a new JackpotResponse RecordBuilder.
   * @return A new JackpotResponse RecordBuilder
   */
  public static com.example.apiserver.objects.JackpotResponse.Builder newBuilder() {
    return new com.example.apiserver.objects.JackpotResponse.Builder();
  }

  /**
   * Creates a new JackpotResponse RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new JackpotResponse RecordBuilder
   */
  public static com.example.apiserver.objects.JackpotResponse.Builder newBuilder(com.example.apiserver.objects.JackpotResponse.Builder other) {
    if (other == null) {
      return new com.example.apiserver.objects.JackpotResponse.Builder();
    } else {
      return new com.example.apiserver.objects.JackpotResponse.Builder(other);
    }
  }

  /**
   * Creates a new JackpotResponse RecordBuilder by copying an existing JackpotResponse instance.
   * @param other The existing instance to copy.
   * @return A new JackpotResponse RecordBuilder
   */
  public static com.example.apiserver.objects.JackpotResponse.Builder newBuilder(com.example.apiserver.objects.JackpotResponse other) {
    if (other == null) {
      return new com.example.apiserver.objects.JackpotResponse.Builder();
    } else {
      return new com.example.apiserver.objects.JackpotResponse.Builder(other);
    }
  }

  /**
   * RecordBuilder for JackpotResponse instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<JackpotResponse>
    implements org.apache.avro.data.RecordBuilder<JackpotResponse> {

    /** The ID of a specific Jackpot pool */
    private java.lang.CharSequence jackpotPoolId;
    /** The geographic region of the Jackpot pool */
    private java.lang.CharSequence region;
    /** The current winnable Jackpot */
    private int jackpot;
    /** The status of the request for a Jackpot */
    private java.lang.CharSequence status;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.example.apiserver.objects.JackpotResponse.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.jackpotPoolId)) {
        this.jackpotPoolId = data().deepCopy(fields()[0].schema(), other.jackpotPoolId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.region)) {
        this.region = data().deepCopy(fields()[1].schema(), other.region);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.jackpot)) {
        this.jackpot = data().deepCopy(fields()[2].schema(), other.jackpot);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.status)) {
        this.status = data().deepCopy(fields()[3].schema(), other.status);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing JackpotResponse instance
     * @param other The existing instance to copy.
     */
    private Builder(com.example.apiserver.objects.JackpotResponse other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.jackpotPoolId)) {
        this.jackpotPoolId = data().deepCopy(fields()[0].schema(), other.jackpotPoolId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.region)) {
        this.region = data().deepCopy(fields()[1].schema(), other.region);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.jackpot)) {
        this.jackpot = data().deepCopy(fields()[2].schema(), other.jackpot);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.status)) {
        this.status = data().deepCopy(fields()[3].schema(), other.status);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'jackpotPoolId' field.
      * The ID of a specific Jackpot pool
      * @return The value.
      */
    public java.lang.CharSequence getJackpotPoolId() {
      return jackpotPoolId;
    }


    /**
      * Sets the value of the 'jackpotPoolId' field.
      * The ID of a specific Jackpot pool
      * @param value The value of 'jackpotPoolId'.
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder setJackpotPoolId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.jackpotPoolId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'jackpotPoolId' field has been set.
      * The ID of a specific Jackpot pool
      * @return True if the 'jackpotPoolId' field has been set, false otherwise.
      */
    public boolean hasJackpotPoolId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'jackpotPoolId' field.
      * The ID of a specific Jackpot pool
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder clearJackpotPoolId() {
      jackpotPoolId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'region' field.
      * The geographic region of the Jackpot pool
      * @return The value.
      */
    public java.lang.CharSequence getRegion() {
      return region;
    }


    /**
      * Sets the value of the 'region' field.
      * The geographic region of the Jackpot pool
      * @param value The value of 'region'.
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder setRegion(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.region = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'region' field has been set.
      * The geographic region of the Jackpot pool
      * @return True if the 'region' field has been set, false otherwise.
      */
    public boolean hasRegion() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'region' field.
      * The geographic region of the Jackpot pool
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder clearRegion() {
      region = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'jackpot' field.
      * The current winnable Jackpot
      * @return The value.
      */
    public int getJackpot() {
      return jackpot;
    }


    /**
      * Sets the value of the 'jackpot' field.
      * The current winnable Jackpot
      * @param value The value of 'jackpot'.
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder setJackpot(int value) {
      validate(fields()[2], value);
      this.jackpot = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'jackpot' field has been set.
      * The current winnable Jackpot
      * @return True if the 'jackpot' field has been set, false otherwise.
      */
    public boolean hasJackpot() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'jackpot' field.
      * The current winnable Jackpot
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder clearJackpot() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'status' field.
      * The status of the request for a Jackpot
      * @return The value.
      */
    public java.lang.CharSequence getStatus() {
      return status;
    }


    /**
      * Sets the value of the 'status' field.
      * The status of the request for a Jackpot
      * @param value The value of 'status'.
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder setStatus(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.status = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'status' field has been set.
      * The status of the request for a Jackpot
      * @return True if the 'status' field has been set, false otherwise.
      */
    public boolean hasStatus() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'status' field.
      * The status of the request for a Jackpot
      * @return This builder.
      */
    public com.example.apiserver.objects.JackpotResponse.Builder clearStatus() {
      status = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JackpotResponse build() {
      try {
        JackpotResponse record = new JackpotResponse();
        record.jackpotPoolId = fieldSetFlags()[0] ? this.jackpotPoolId : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.region = fieldSetFlags()[1] ? this.region : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.jackpot = fieldSetFlags()[2] ? this.jackpot : (java.lang.Integer) defaultValue(fields()[2]);
        record.status = fieldSetFlags()[3] ? this.status : (java.lang.CharSequence) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<JackpotResponse>
    WRITER$ = (org.apache.avro.io.DatumWriter<JackpotResponse>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<JackpotResponse>
    READER$ = (org.apache.avro.io.DatumReader<JackpotResponse>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.jackpotPoolId);

    out.writeString(this.region);

    out.writeInt(this.jackpot);

    out.writeString(this.status);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.jackpotPoolId = in.readString(this.jackpotPoolId instanceof Utf8 ? (Utf8)this.jackpotPoolId : null);

      this.region = in.readString(this.region instanceof Utf8 ? (Utf8)this.region : null);

      this.jackpot = in.readInt();

      this.status = in.readString(this.status instanceof Utf8 ? (Utf8)this.status : null);

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.jackpotPoolId = in.readString(this.jackpotPoolId instanceof Utf8 ? (Utf8)this.jackpotPoolId : null);
          break;

        case 1:
          this.region = in.readString(this.region instanceof Utf8 ? (Utf8)this.region : null);
          break;

        case 2:
          this.jackpot = in.readInt();
          break;

        case 3:
          this.status = in.readString(this.status instanceof Utf8 ? (Utf8)this.status : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










