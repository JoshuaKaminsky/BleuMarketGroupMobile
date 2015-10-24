package com.mobile.bmg.model.api;

/**
 * Created by Josh on 9/13/15.
 */
public class Urls {

    private static final String baseUrl = "https://api.bleumarketgroup.org/";

    private static final String usersUrl = "v1/users/";

    private static final String loginUrl = "v1/login";

    private static final String logoutUrl = "v1/logout";

    private static final String organizationsUrl = "v1/organizations/";

    private static final String opportunitiesUrl = "v1/opportunities/";

    private static final String cartUrl = "v1/cart/";

    private static final String clearUrl = "clear/";

    private static final String checkoutUrl = "checkout/";

    private static final String transactionUrl = "v1/transaction";

    private static final String ourUrl = "v1/our";

    private static final String meUrl = "me";

    private static final String totalsUrl = "totals";

    private static final String tagsUrl = "tags";

    private static final String categoriesUrl = "categories";

    private static final String aboutUrl = "about";

    private static final String eventsUrl = "events";

    private static final String newsUrl = "news";

    public static String createUser() {
        return String.format("%s%s", baseUrl, usersUrl);
    }

    public static String updateUser(int id) {
        return String.format("%s%s%d", baseUrl, usersUrl, id);
    }

    public static String deleteUser(int id) {
        return String.format("%s%s%d", baseUrl, usersUrl, id);
    }

    public static String getUser(int id) {
        return String.format("%s%s%d", baseUrl, usersUrl, id);
    }

    public static String getLoggedInUser() {
        return String.format("%s%s%s", baseUrl, usersUrl, meUrl);
    }

    public static String getLogin() {
        return String.format("%s%s", baseUrl, loginUrl);
    }

    public static String getLogout() {
        return String.format("%s%s", baseUrl, logoutUrl);
    }

    public static String getOrganizations() {
        return String.format("%s%s", baseUrl, organizationsUrl);
    }

    public static String getOrganizationsByLocationDistanceAndPage(String search, int distance, int page) {
        String url = getOrganizations();

        url = String.format("%s?page=%d", url, page > 0 ? page : 1);

        if(search != null && !search.isEmpty())
            url = String.format("%s&search=%s", url, search);

        if(distance > 0) {
            url = String.format("%s&distance=%d", url, distance);
        }

        return url;
    }

    public static String getOrganizationsByLocationAndKeyword(String search, String keyword) {
        String url = getOrganizations();

        if(search != null && !search.isEmpty())
            url = String.format("%s?search=%s", url, search);

        if(keyword != null && !keyword.isEmpty()) {
            char conjunction = (search == null || search.isEmpty()) ? '?' : '&';

            url = String.format("%s%skeyword=%s", url, conjunction, keyword);
        }

        return url;
    }

    public static String getOrganization(int id) {
        return String.format("%s%s%d", baseUrl, organizationsUrl, id);
    }

    public static String getOrganizationUsers(int id){
        return String.format("%s/%s", getOrganization(id), "users");
    }

    public static String getOrganizationByTag(String tag, int page) {
        return String.format("%s%s?tag_list=%s&page=%d", baseUrl, organizationsUrl, tag, page > 0 ? page : 1);
    }

    public static String getOrganizationTotals(int id) {
        return String.format("%s/%s", getOrganization(id), totalsUrl);
    }

    public static String getOrganizationTags() {
        return String.format("%s%s%s", baseUrl, organizationsUrl, tagsUrl);
    }

    public static String getFeaturedOrganizations() {
        return String.format("%s%s?featured=1", baseUrl, organizationsUrl);
    }

    public static String getUserOrganizations(int userId) {
        return String.format("%s/organizations", getUser(userId));
    }

    public static String getUserLikes(int userId) {
        return String.format("%s/likes", getUser(userId));
    }

    public static String addOrganizationLike(int organizationId) {
        return String.format("%s/like", getOrganization(organizationId));
    }

    public static String removeOrganizationLike(int organizationId) {
        return String.format("%s/unlike", getOrganization(organizationId));
    }

    public static String getOpportunities(int page) {
        String url = String.format("%s%s", baseUrl, opportunitiesUrl);

        url = String.format("%s?page=%d", url, page > 0 ? page : 1);

        return url;
    }

    public static String getOpportunitiesByCategory(String category, int perPage, int page) {
        String url = getOpportunities(page);

        if(category == null || category.isEmpty())
            return url;

        url = String.format("%s&category_list=%s", url, category);

        if(perPage > 0)
            url = String.format("%s&per_page=%d", url, perPage);

        return url;
    }

    public static String getOpportunityCategories() {
        return String.format("%s%s%s", baseUrl, opportunitiesUrl, categoriesUrl);
    }

    public static String getOpportunityTags() {
        return String.format("%s%s%s", baseUrl, opportunitiesUrl, tagsUrl);
    }

    public static String getOpportunity(int id) {
        return String.format("%s%s%d", baseUrl, opportunitiesUrl, id);
    }

    public static String getAbout() {
        return String.format("%s%s%s", baseUrl, ourUrl, aboutUrl);
    }

    public static String getEvents() {
        return String.format("%s%s%s", baseUrl, ourUrl, eventsUrl);
    }

    public static String getNews() {
        return String.format("%s%s%s", baseUrl, ourUrl, newsUrl);
    }

    public static String getUserCart(){return String.format("%s%s", baseUrl, cartUrl);}

    public static String addToUserCart(){return String.format("%s%s", baseUrl, cartUrl);}

    public static String removeFromUserCart(int itemId) { return String.format("%s%s%d", baseUrl, cartUrl, itemId);}

    public static String deleteUserCart() { return String.format("%s%s%s", baseUrl, cartUrl, clearUrl);}

    public static String chechout() {return String.format("%s%s%s", baseUrl, cartUrl, checkoutUrl);}
}
