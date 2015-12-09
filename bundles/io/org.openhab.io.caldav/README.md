# CalDAV IO Binding

## Implementation Notes

The specific versions inside the MANIFEST.MF are required 
because of the used libraries for querying the calendar entries from the caldav server.
If they are not specified the osgi runtime will use newer versions which will not work. 