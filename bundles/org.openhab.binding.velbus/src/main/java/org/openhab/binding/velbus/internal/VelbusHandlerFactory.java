/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.velbus.internal;

import static org.openhab.binding.velbus.internal.VelbusBindingConstants.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.openhab.binding.velbus.internal.discovery.VelbusThingDiscoveryService;
import org.openhab.binding.velbus.internal.handler.VelbusBlindsHandler;
import org.openhab.binding.velbus.internal.handler.VelbusBridgeHandler;
import org.openhab.binding.velbus.internal.handler.VelbusDimmerHandler;
import org.openhab.binding.velbus.internal.handler.VelbusNetworkBridgeHandler;
import org.openhab.binding.velbus.internal.handler.VelbusRelayHandler;
import org.openhab.binding.velbus.internal.handler.VelbusSensorHandler;
import org.openhab.binding.velbus.internal.handler.VelbusSensorWithAlarmClockHandler;
import org.openhab.binding.velbus.internal.handler.VelbusSerialBridgeHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMB1TSHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMB4ANHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMB7INHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMBELHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMBELOHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMBGPHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMBGPOHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMBMeteoHandler;
import org.openhab.binding.velbus.internal.handler.VelbusVMBPIROHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link VelbusHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Cedric Boon - Initial contribution
 */
@NonNullByDefault
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.velbus")
public class VelbusHandlerFactory extends BaseThingHandlerFactory {
    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();

    private @NonNullByDefault({}) SerialPortManager serialPortManager;

    @Reference
    protected void setSerialPortManager(final SerialPortManager serialPortManager) {
        this.serialPortManager = serialPortManager;
    }

    protected void unsetSerialPortManager(final SerialPortManager serialPortManager) {
        this.serialPortManager = null;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return BRIDGE_THING_TYPES_UIDS.contains(thingTypeUID) || SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        ThingHandler thingHandler = null;

        if (BRIDGE_THING_TYPES_UIDS.contains(thingTypeUID)) {
            VelbusBridgeHandler velbusBridgeHandler = thingTypeUID.equals(NETWORK_BRIDGE_THING_TYPE)
                    ? new VelbusNetworkBridgeHandler((Bridge) thing)
                    : new VelbusSerialBridgeHandler((Bridge) thing, serialPortManager);
            registerDiscoveryService(velbusBridgeHandler);
            thingHandler = velbusBridgeHandler;
        } else if (VelbusRelayHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusRelayHandler(thing);
        } else if (VelbusDimmerHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusDimmerHandler(thing);
        } else if (VelbusBlindsHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusBlindsHandler(thing);
        } else if (VelbusSensorHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusSensorHandler(thing);
        } else if (VelbusSensorWithAlarmClockHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusSensorWithAlarmClockHandler(thing);
        } else if (VelbusVMBMeteoHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMBMeteoHandler(thing);
        } else if (VelbusVMBGPHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMBGPHandler(thing);
        } else if (VelbusVMBGPOHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMBGPOHandler(thing);
        } else if (VelbusVMBPIROHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMBPIROHandler(thing);
        } else if (VelbusVMB7INHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMB7INHandler(thing);
        } else if (VelbusVMBELHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMBELHandler(thing);
        } else if (VelbusVMBELOHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMBELOHandler(thing);
        } else if (VelbusVMB4ANHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMB4ANHandler(thing);
        } else if (VelbusVMB1TSHandler.SUPPORTED_THING_TYPES.contains(thingTypeUID)) {
            thingHandler = new VelbusVMB1TSHandler(thing);
        }

        return thingHandler;
    }

    @Override
    protected void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof VelbusBridgeHandler) {
            unregisterDiscoveryService((VelbusBridgeHandler) thingHandler);
        }
    }

    private synchronized void registerDiscoveryService(VelbusBridgeHandler bridgeHandler) {
        VelbusThingDiscoveryService discoveryService = new VelbusThingDiscoveryService(bridgeHandler);
        discoveryService.activate();
        this.discoveryServiceRegs.put(bridgeHandler.getThing().getUID(), bundleContext
                .registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<String, Object>()));
    }

    private synchronized void unregisterDiscoveryService(VelbusBridgeHandler bridgeHandler) {
        ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.remove(bridgeHandler.getThing().getUID());
        if (serviceReg != null) {
            VelbusThingDiscoveryService service = (VelbusThingDiscoveryService) bundleContext
                    .getService(serviceReg.getReference());
            serviceReg.unregister();
            if (service != null) {
                service.deactivate();
            }
        }
    }
}
