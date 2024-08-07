/*
 * This file is part of Limbo.
 *
 * Copyright (C) 2023. LoohpJames <jamesloohp@gmail.com>
 * Copyright (C) 2023. Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.loohp.limbo.network.protocol.packets;

import com.loohp.limbo.registry.PacketRegistry;
import com.loohp.limbo.registry.RegistryCustom;
import com.loohp.limbo.utils.DataTypeIO;
import net.kyori.adventure.key.Key;
import net.querz.nbt.tag.CompoundTag;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ClientboundRegistryDataPacket extends PacketOut {

    private final RegistryCustom registry;

    public ClientboundRegistryDataPacket(RegistryCustom registry) {
        this.registry = registry;
    }

    public RegistryCustom getRegistry() {
        return registry;
    }

    @Override
    public byte[] serializePacket() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        DataOutputStream output = new DataOutputStream(buffer);
        output.writeByte(PacketRegistry.getPacketId(getClass()));

        DataTypeIO.writeString(output, registry.getIdentifier().asString(), StandardCharsets.UTF_8);
        DataTypeIO.writeVarInt(output, registry.getEntries().size());
        for (Map.Entry<Key, CompoundTag> entry : registry.getEntries().entrySet()) {
            DataTypeIO.writeString(output, entry.getKey().asString(), StandardCharsets.UTF_8);
            CompoundTag data = entry.getValue();
            if (data == null) {
                output.writeBoolean(false);
            } else {
                output.writeBoolean(true);
                DataTypeIO.writeTag(output, data);
            }
        }

        return buffer.toByteArray();
    }
}
