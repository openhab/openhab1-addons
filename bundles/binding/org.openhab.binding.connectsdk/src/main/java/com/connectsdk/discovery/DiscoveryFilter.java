package com.connectsdk.discovery;

public class DiscoveryFilter {
    String serviceId = null;
    String serviceFilter = null;

    public DiscoveryFilter(String serviceId, String serviceFilter) {
        this.serviceId = serviceId;
        this.serviceFilter = serviceFilter;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceFilter() {
        return serviceFilter;
    }

    public void setServiceFilter(String serviceFilter) {
        this.serviceFilter = serviceFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscoveryFilter that = (DiscoveryFilter) o;

        if (serviceFilter != null ? !serviceFilter.equals(that.serviceFilter) : that.serviceFilter != null)
            return false;
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = serviceId != null ? serviceId.hashCode() : 0;
        result = 31 * result + (serviceFilter != null ? serviceFilter.hashCode() : 0);
        return result;
    }
}
