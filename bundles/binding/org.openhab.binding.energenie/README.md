# Energenie Binding

The openHAB Energenie binding allows you to send commands to multiple Gembird energenie PMS-LAN power extenders.

## Binding Configuration

This binding can be configured in the file `services/energenie.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<pmsId1>`.host |  |   Yes    | IP address of the first PMS-LAN to control |
| `<pmsId1>`.password | 1 |   No    | Password to login to the first PMS-LAN |
| `<pmsId2>`.host |  |   Yes    | Host of the second PMS-LAN to control |
| `<pmsId2>`.password | |   No  | Password to login to the second PMS-LAN |

### Example

```
pms1.host=192.168.1.100
bridge1.password=1
```

## Item Configuration

The syntax of the configuration strings accepted is the following:

```
energenie="<deviceId>;<socketID>"
```

The `<deviceId>` corresponds to the PMS-LAN which is defined in the configuration.

The `<socketID>` corresponds to the number of the socket you want to control (1-4).

Examples, how to configure your items in your items file:

```
Switch Light_OfficeDesk     {energenie="pms1;1"}
```
