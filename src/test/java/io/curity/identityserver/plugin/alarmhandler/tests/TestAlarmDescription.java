package io.curity.identityserver.plugin.alarmhandler.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import se.curity.identityserver.sdk.alarm.AlarmDescription;
import se.curity.identityserver.sdk.alarm.ResourceType;

/*
 * An alarm description for testing, based on a real HTTP client failure
 */
public final class TestAlarmDescription implements AlarmDescription {

    @Override
    public String getDashboardLink() {
        return "http://localhost:6749/admin/#/alarm?resource=%2Fbase%3Aenvironments%2Fenvironment%2Fservices%2Fruntime-service%5Bid%3D%271THAkvyM%27%5D%2F..%2F..%2F..%2F..%2Fbase%3Afacilities%2Fhttp%2Fclient%5Bid%3D%27custom-api-client%27%5D&alarm-type-id=alde%3Afailed-connection&alarm-type-qualifier=http%3A%2F%2Flocalhostxxx%3A8002";
    }

    @Override
    public String getBriefDescription() {
        return "The HTTP client has failed to connect to a remote host";
    }

    @Override
    public List<String> getDetailedDescription() {
        List<String> details = new ArrayList<>();
        details.add(
                "This alarm can be triggered by faulty configuration, faulty networking or a faulty remote service.");
        return details;
    }

    @Override
    public Map<ResourceType, ImpactedResourceGroup> getImpactedResources() {

        HashMap<ResourceType, ImpactedResourceGroup> resources = new HashMap<>();

        Set<String> providers = new HashSet<>();
        providers.add("api-claims-provider");
        resources.put(
                ResourceType.CLAIMS_VALUE_PROVIDER,
                new ImpactedResourceGroup("Claims Value Providers", providers));

        Set<String> clients = new HashSet<>();
        clients.add("web-client");
        clients.add("ios-client");
        clients.add("android-client");
        resources.put(ResourceType.OAUTH_CLIENT, new ImpactedResourceGroup("OAuth Clients", clients));

        Set<String> profiles = new HashSet<>();
        profiles.add("token-service");
        resources.put(ResourceType.PROFILE, new ImpactedResourceGroup("Profiles", profiles));

        return resources;
    }

    @Override
    public List<String> getSuggestedActions() {

        List<String> actions = new ArrayList<>();
        actions.add("Verify that the remote host is running, and is connected to a network reachable by the affected Curity Identity Server runtime node on the configured port.");
        actions.add("Inspect the logs of the remote host, or contact support of the service provider.");
        actions.add("Inspect the configuration and logs of any intermediate network equipment, such as routers and proxies.");
        actions.add("Inspect the logs of the affected Curity Identity Server runtime node at the time of this alarm.");
        actions.add("Verify that any intermediate proxies and firewalls allow the expected connection.");
        actions.add("Verify the configured connection settings for this resource. Such as host, port, client certificate and trusted server certificates.");
        return actions;
    }

    @Override
    public String asMarkdown() {
        return null;
    }

    @Override
    public Map<String, Object> asMap() {
        return null;
    }
}
