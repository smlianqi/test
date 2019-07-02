// automatically generated by the FlatBuffers compiler, do not modify

package com.elex.im.message.flat;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class CreateChatRoomUp extends Table {
  public static CreateChatRoomUp getRootAsCreateChatRoomUp(ByteBuffer _bb) { return getRootAsCreateChatRoomUp(_bb, new CreateChatRoomUp()); }
  public static CreateChatRoomUp getRootAsCreateChatRoomUp(ByteBuffer _bb, CreateChatRoomUp obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
  public CreateChatRoomUp __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String admin() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer adminAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public String uid(int j) { int o = __offset(6); return o != 0 ? __string(__vector(o) + j * 4) : null; }
  public int uidLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }
  public long roomType() { int o = __offset(8); return o != 0 ? (long)bb.getInt(o + bb_pos) & 0xFFFFFFFFL : 0L; }
  public String roomId() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer roomIdAsByteBuffer() { return __vector_as_bytebuffer(10, 1); }

  public static int createCreateChatRoomUp(FlatBufferBuilder builder,
      int adminOffset,
      int uidOffset,
      long roomType,
      int roomIdOffset) {
    builder.startObject(4);
    CreateChatRoomUp.addRoomId(builder, roomIdOffset);
    CreateChatRoomUp.addRoomType(builder, roomType);
    CreateChatRoomUp.addUid(builder, uidOffset);
    CreateChatRoomUp.addAdmin(builder, adminOffset);
    return CreateChatRoomUp.endCreateChatRoomUp(builder);
  }

  public static void startCreateChatRoomUp(FlatBufferBuilder builder) { builder.startObject(4); }
  public static void addAdmin(FlatBufferBuilder builder, int adminOffset) { builder.addOffset(0, adminOffset, 0); }
  public static void addUid(FlatBufferBuilder builder, int uidOffset) { builder.addOffset(1, uidOffset, 0); }
  public static int createUidVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startUidVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addRoomType(FlatBufferBuilder builder, long roomType) { builder.addInt(2, (int)roomType, (int)0L); }
  public static void addRoomId(FlatBufferBuilder builder, int roomIdOffset) { builder.addOffset(3, roomIdOffset, 0); }
  public static int endCreateChatRoomUp(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

