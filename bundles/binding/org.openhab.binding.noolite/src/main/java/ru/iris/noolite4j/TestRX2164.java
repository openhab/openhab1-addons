/*
 * Copyright 2014 Nikolay A. Viguro
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

package ru.iris.noolite4j;

import org.openhab.binding.noolite.internal.noolite4j.RX2164;
import org.openhab.binding.noolite.internal.noolite4j.watchers.BatteryState;
import org.openhab.binding.noolite.internal.noolite4j.watchers.CommandType;
import org.openhab.binding.noolite.internal.noolite4j.watchers.Notification;
import org.openhab.binding.noolite.internal.noolite4j.watchers.SensorType;
import org.openhab.binding.noolite.internal.noolite4j.watchers.Watcher;


public class TestRX2164 {

   public static void main(String[] ARGV)
   {
       RX2164 rx = new RX2164();

       Watcher watcher = new Watcher() {
           @Override
           public void onNotification(Notification notification) {
               System.out.println("----------------------------------");
               System.out.println("RX2164 получил команду: ");
               System.out.println("Устройство: " + notification.getChannel());
               System.out.println("Команда: " + notification.getType().name());
               System.out.println("Формат данных к команде: " + notification.getDataFormat().name());

               // Передаются данные с датчика
               if(notification.getType() == CommandType.TEMP_HUMI)
               {
                   SensorType sensor = (SensorType)notification.getValue("sensortype");
                   BatteryState battery = (BatteryState)notification.getValue("battery");

                   System.out.println("Температура: " + notification.getValue("temp"));
                   System.out.println("Влажность: " + notification.getValue("humi"));
                   System.out.println("Тип датчика: " + sensor.name());
                   System.out.println("Состояние батареи: " + battery.name());

                   if(sensor == SensorType.PT111)
                   {
                       System.out.println("Обнаружен датчик температуры и влажности");
                   }
                   else if(sensor == SensorType.PT112)
                   {
                       System.out.println("Обнаружен датчик температуры");
                   }
               }
           }
       };

       rx.open();
       rx.addWatcher(watcher);
       rx.start();
   }

}
