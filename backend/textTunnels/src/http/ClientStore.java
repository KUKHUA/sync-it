/*
 * ChangeDetector -  detects modifications, deletions, or creations of files and folders.
 * Copyright (C) 2025 KUKHUA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * MODIFICATIONS:
 * - added method getChannelClients() to retrive httpexchnages.
 * - made everything thread-safeish
 * - removed existing mehtod to get cleints.
 */

package http;

import com.sun.net.httpserver.HttpExchange;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;
import java.util.List;

/** 
 * The {@code ClientStore} class is a singleton that manages a collection of active HTTP client connections.
 * It allows adding new clients and retrieving the list of currently connected clients.
 */
public final class ClientStore {

    private ConcurrentHashMap<String, List<HttpExchange>> clients = new ConcurrentHashMap<>();

    private static ClientStore instance = null;

    private ClientStore(){}

    public void addClient(String channel, HttpExchange exchange) {
        clients.computeIfAbsent(channel, key -> new CopyOnWriteArrayList<>()).add(exchange);
    }

    public List<HttpExchange> getChannelClients(String channel){
        return clients.getOrDefault(channel, new CopyOnWriteArrayList<>());
    }

    public static ClientStore get(){
        synchronized (ClientStore.class) {
            if (instance == null) instance = new ClientStore();
        }
        return instance;
    }
}
