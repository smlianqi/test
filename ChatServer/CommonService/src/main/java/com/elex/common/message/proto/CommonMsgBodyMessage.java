// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CommonMsgBody.proto

package com.elex.common.message.proto;

public final class CommonMsgBodyMessage {
  private CommonMsgBodyMessage() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface CommonMsgBodyOrBuilder extends
      // @@protoc_insertion_point(interface_extends:CommonMsgBody)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *指令版本号
     * </pre>
     *
     * <code>uint32 version = 1;</code>
     */
    int getVersion();

    /**
     * <pre>
     *指令id
     * </pre>
     *
     * <code>uint32 commandId = 2;</code>
     */
    int getCommandId();

    /**
     * <pre>
     *消息内容
     * </pre>
     *
     * <code>bytes content = 3;</code>
     */
    com.google.protobuf.ByteString getContent();
  }
  /**
   * <pre>
   *通用消息体
   * </pre>
   *
   * Protobuf type {@code CommonMsgBody}
   */
  public  static final class CommonMsgBody extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:CommonMsgBody)
      CommonMsgBodyOrBuilder {
    // Use CommonMsgBody.newBuilder() to construct.
    private CommonMsgBody(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private CommonMsgBody() {
      version_ = 0;
      commandId_ = 0;
      content_ = com.google.protobuf.ByteString.EMPTY;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private CommonMsgBody(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              version_ = input.readUInt32();
              break;
            }
            case 16: {

              commandId_ = input.readUInt32();
              break;
            }
            case 26: {

              content_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.elex.common.message.proto.CommonMsgBodyMessage.internal_static_CommonMsgBody_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.elex.common.message.proto.CommonMsgBodyMessage.internal_static_CommonMsgBody_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody.class, com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody.Builder.class);
    }

    public static final int VERSION_FIELD_NUMBER = 1;
    private int version_;
    /**
     * <pre>
     *指令版本号
     * </pre>
     *
     * <code>uint32 version = 1;</code>
     */
    public int getVersion() {
      return version_;
    }

    public static final int COMMANDID_FIELD_NUMBER = 2;
    private int commandId_;
    /**
     * <pre>
     *指令id
     * </pre>
     *
     * <code>uint32 commandId = 2;</code>
     */
    public int getCommandId() {
      return commandId_;
    }

    public static final int CONTENT_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString content_;
    /**
     * <pre>
     *消息内容
     * </pre>
     *
     * <code>bytes content = 3;</code>
     */
    public com.google.protobuf.ByteString getContent() {
      return content_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (version_ != 0) {
        output.writeUInt32(1, version_);
      }
      if (commandId_ != 0) {
        output.writeUInt32(2, commandId_);
      }
      if (!content_.isEmpty()) {
        output.writeBytes(3, content_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (version_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, version_);
      }
      if (commandId_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, commandId_);
      }
      if (!content_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, content_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody)) {
        return super.equals(obj);
      }
      com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody other = (com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody) obj;

      boolean result = true;
      result = result && (getVersion()
          == other.getVersion());
      result = result && (getCommandId()
          == other.getCommandId());
      result = result && getContent()
          .equals(other.getContent());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + VERSION_FIELD_NUMBER;
      hash = (53 * hash) + getVersion();
      hash = (37 * hash) + COMMANDID_FIELD_NUMBER;
      hash = (53 * hash) + getCommandId();
      hash = (37 * hash) + CONTENT_FIELD_NUMBER;
      hash = (53 * hash) + getContent().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     *通用消息体
     * </pre>
     *
     * Protobuf type {@code CommonMsgBody}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:CommonMsgBody)
        com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.elex.common.message.proto.CommonMsgBodyMessage.internal_static_CommonMsgBody_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.elex.common.message.proto.CommonMsgBodyMessage.internal_static_CommonMsgBody_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody.class, com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody.Builder.class);
      }

      // Construct using com.elex.im.common.message.proto.CommonMsgBodyMessage.CommonMsgBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        version_ = 0;

        commandId_ = 0;

        content_ = com.google.protobuf.ByteString.EMPTY;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.elex.common.message.proto.CommonMsgBodyMessage.internal_static_CommonMsgBody_descriptor;
      }

      public com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody getDefaultInstanceForType() {
        return com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody.getDefaultInstance();
      }

      public com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody build() {
        com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody buildPartial() {
        com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody result = new com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody(this);
        result.version_ = version_;
        result.commandId_ = commandId_;
        result.content_ = content_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody) {
          return mergeFrom((com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody other) {
        if (other == com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody.getDefaultInstance()) return this;
        if (other.getVersion() != 0) {
          setVersion(other.getVersion());
        }
        if (other.getCommandId() != 0) {
          setCommandId(other.getCommandId());
        }
        if (other.getContent() != com.google.protobuf.ByteString.EMPTY) {
          setContent(other.getContent());
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int version_ ;
      /**
       * <pre>
       *指令版本号
       * </pre>
       *
       * <code>uint32 version = 1;</code>
       */
      public int getVersion() {
        return version_;
      }
      /**
       * <pre>
       *指令版本号
       * </pre>
       *
       * <code>uint32 version = 1;</code>
       */
      public Builder setVersion(int value) {
        
        version_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *指令版本号
       * </pre>
       *
       * <code>uint32 version = 1;</code>
       */
      public Builder clearVersion() {
        
        version_ = 0;
        onChanged();
        return this;
      }

      private int commandId_ ;
      /**
       * <pre>
       *指令id
       * </pre>
       *
       * <code>uint32 commandId = 2;</code>
       */
      public int getCommandId() {
        return commandId_;
      }
      /**
       * <pre>
       *指令id
       * </pre>
       *
       * <code>uint32 commandId = 2;</code>
       */
      public Builder setCommandId(int value) {
        
        commandId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *指令id
       * </pre>
       *
       * <code>uint32 commandId = 2;</code>
       */
      public Builder clearCommandId() {
        
        commandId_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString content_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>bytes content = 3;</code>
       */
      public com.google.protobuf.ByteString getContent() {
        return content_;
      }
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>bytes content = 3;</code>
       */
      public Builder setContent(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        content_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息内容
       * </pre>
       *
       * <code>bytes content = 3;</code>
       */
      public Builder clearContent() {
        
        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:CommonMsgBody)
    }

    // @@protoc_insertion_point(class_scope:CommonMsgBody)
    private static final com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody();
    }

    public static com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<CommonMsgBody>
        PARSER = new com.google.protobuf.AbstractParser<CommonMsgBody>() {
      public CommonMsgBody parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new CommonMsgBody(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<CommonMsgBody> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<CommonMsgBody> getParserForType() {
      return PARSER;
    }

    public com.elex.common.message.proto.CommonMsgBodyMessage.CommonMsgBody getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_CommonMsgBody_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_CommonMsgBody_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023CommonMsgBody.proto\"D\n\rCommonMsgBody\022\017" +
      "\n\007version\030\001 \001(\r\022\021\n\tcommandId\030\002 \001(\r\022\017\n\007co" +
      "ntent\030\003 \001(\014B8\n com.elex.im.common.messag" +
      "e.protoB\024CommonMsgBodyMessageb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_CommonMsgBody_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_CommonMsgBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_CommonMsgBody_descriptor,
        new java.lang.String[] { "Version", "CommandId", "Content", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
