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
public class JackpotRequest extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 3497658832311578350L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"JackpotRequest\",\"namespace\":\"com.example.apiserver.objects\",\"fields\":[{\"name\":\"jackpotPoolId\",\"type\":[\"null\",\"string\"],\"doc\":\"The ID of a specific Jackpot pool\"}],\"version\":\"1\"}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<JackpotRequest> ENCODER =
      new BinaryMessageEncoder<JackpotRequest>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<JackpotRequest> DECODER =
      new BinaryMessageDecoder<JackpotRequest>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<JackpotRequest> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<JackpotRequest> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<JackpotRequest> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<JackpotRequest>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this JackpotRequest to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a JackpotRequest from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a JackpotRequest instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static JackpotRequest fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** The ID of a specific Jackpot pool */
  private java.lang.CharSequence jackpotPoolId;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public JackpotRequest() {}

  /**
   * All-args constructor.
   * @param jackpotPoolId The ID of a specific Jackpot pool
   */
  public JackpotRequest(java.lang.CharSequence jackpotPoolId) {
    this.jackpotPoolId = jackpotPoolId;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return jackpotPoolId;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: jackpotPoolId = (java.lang.CharSequence)value$; break;
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
   * Creates a new JackpotRequest RecordBuilder.
   * @return A new JackpotRequest RecordBuilder
   */
  public static com.example.apiserver.objects.JackpotRequest.Builder newBuilder() {
    return new com.example.apiserver.objects.JackpotRequest.Builder();
  }

  /**
   * Creates a new JackpotRequest RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new JackpotRequest RecordBuilder
   */
  public static com.example.apiserver.objects.JackpotRequest.Builder newBuilder(com.example.apiserver.objects.JackpotRequest.Builder other) {
    if (other == null) {
      return new com.example.apiserver.objects.JackpotRequest.Builder();
    } else {
      return new com.example.apiserver.objects.JackpotRequest.Builder(other);
    }
  }

  /**
   * Creates a new JackpotRequest RecordBuilder by copying an existing JackpotRequest instance.
   * @param other The existing instance to copy.
   * @return A new JackpotRequest RecordBuilder
   */
  public static com.example.apiserver.objects.JackpotRequest.Builder newBuilder(com.example.apiserver.objects.JackpotRequest other) {
    if (other == null) {
      return new com.example.apiserver.objects.JackpotRequest.Builder();
    } else {
      return new com.example.apiserver.objects.JackpotRequest.Builder(other);
    }
  }

  /**
   * RecordBuilder for JackpotRequest instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<JackpotRequest>
    implements org.apache.avro.data.RecordBuilder<JackpotRequest> {

    /** The ID of a specific Jackpot pool */
    private java.lang.CharSequence jackpotPoolId;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.example.apiserver.objects.JackpotRequest.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.jackpotPoolId)) {
        this.jackpotPoolId = data().deepCopy(fields()[0].schema(), other.jackpotPoolId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing JackpotRequest instance
     * @param other The existing instance to copy.
     */
    private Builder(com.example.apiserver.objects.JackpotRequest other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.jackpotPoolId)) {
        this.jackpotPoolId = data().deepCopy(fields()[0].schema(), other.jackpotPoolId);
        fieldSetFlags()[0] = true;
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
    public com.example.apiserver.objects.JackpotRequest.Builder setJackpotPoolId(java.lang.CharSequence value) {
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
    public com.example.apiserver.objects.JackpotRequest.Builder clearJackpotPoolId() {
      jackpotPoolId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JackpotRequest build() {
      try {
        JackpotRequest record = new JackpotRequest();
        record.jackpotPoolId = fieldSetFlags()[0] ? this.jackpotPoolId : (java.lang.CharSequence) defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<JackpotRequest>
    WRITER$ = (org.apache.avro.io.DatumWriter<JackpotRequest>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<JackpotRequest>
    READER$ = (org.apache.avro.io.DatumReader<JackpotRequest>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    if (this.jackpotPoolId == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeString(this.jackpotPoolId);
    }

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      if (in.readIndex() != 1) {
        in.readNull();
        this.jackpotPoolId = null;
      } else {
        this.jackpotPoolId = in.readString(this.jackpotPoolId instanceof Utf8 ? (Utf8)this.jackpotPoolId : null);
      }

    } else {
      for (int i = 0; i < 1; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          if (in.readIndex() != 1) {
            in.readNull();
            this.jackpotPoolId = null;
          } else {
            this.jackpotPoolId = in.readString(this.jackpotPoolId instanceof Utf8 ? (Utf8)this.jackpotPoolId : null);
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










