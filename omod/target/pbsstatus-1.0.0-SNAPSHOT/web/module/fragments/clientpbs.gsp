<%

    ui.includeCss("pbsstatus", "bootstrap.min.css")
    ui.includeJavascript("pbsstatus", "bootstrap.min.js")

    def id = config.id
%>
<h1 style="text-align: center;">CLIENT FINGERPRINT ANALYSIS</h1>
<h3 style="text-align: center;">PEPFAR ID: <%=pepfarId%></h3>
<% puuid = "8cc7974f-4f30-4a40-84d9-7f49a4a2818b" %>
<div style="text-align: center"><a href="/<%=ui.contextPath()%>/nigeriaemr/biometricform.page?patientId=<%=puuid%>&returnUrl=%2Fopenmrs%2Fcoreapps%2Fclinicianfacing%2Fpatient.page%3FpatientId%3D<%=puuid%>%26" class="btn btn-outline-info">Recapture</a> </div>
<hr style="border: solid 2px gray">
<h2 style="text-align: center; font-weight: bold;">NDR Status</h2>
<table class="table table-striped" width="98%" style="width: 98% !important;  margin: 1% !important">
    <thead>
    <tr>
        <th>Match Status</th>
        <th>Recapture Date</th>
        <th>Baseline Replaced</th>
        <th>Other Details</th>
    </tr>
    </thead>


    <tbody>

    <%
        def myObjects = new groovy.json.JsonSlurper().parseText(ndrStatus);

        if (!myObjects.isEmpty()) {

        String otherinfo = myObjects.get(0).get("other_info");

        if(myObjects.get(0).get("match_outcome")=="Match" && myObjects.get(0).get("otherinfo")==null){
            otherinfo = "DO NOT replace when mismatched since this client has matched on NDR, rather, recapture by placing the finger properly."
        }
    %>
        <tr>
            <td>${myObjects.get(0).get("match_outcome")}</td>
            <td>${myObjects.get(0).get("recapture_date")}</td>
            <td>${myObjects.get(0).get("baseline_replaced")}</td>
            <td style="color: red;">${otherinfo}</td>

        </tr>
    <% } else { %>
    <tr>
        <td colspan="4">There is no record found on the NDR</td>
    </tr>
    <% } %>
    </tbody>
</table>

<hr style="border: solid 2px gray">
<div class="row">
    <div class="col-md-5">

    <h2>Current Recapture Comparison with Baseline</h2>

    <div class="row" style="width: 100% !important;">

        <table class="table table-striped" width="100%" style="width: 100% !important;  margin: 1% !important">
            <thead>
            <tr>
                <th>#</th>
                <th>Group ID</th>
                <th>Finger Position</th>

                <th>Match Status</th>
                <th>BR Status</th>
                <th>Date Created</th>
            </tr>
            </thead>


            <tbody>
            <% if (current_recapture) { %>
                <% for (int i = 0; i < current_recapture.size(); i++) { %>

                <tr>
                    <td><%= i+1 %></td>
                    <td>${ current_recapture.get(i).get("group_id")}</td>
                    <td>${ current_recapture.get(i).get("finger_position") }</td>
                    <td <% if(current_recapture.get(i).get("match_status")=="NoMatch"){ %> <%= "style='background-color: red''"%> <% }%>>${ current_recapture.get(i).get("match_status") }</td>
                    <td>${ current_recapture.get(i).get("br_status")}</td>
                    <td>${ current_recapture.get(i).get("date_created")}</td>
                </tr>
                <% } %>
            <% } else { %>
            <tr>
                <td colspan="8">There is no current recapture for this client</td>
            </tr>
            <% } %>
            </tbody>
        </table>

    </div>
    </div>

    <div class="col-md-7">
        <h2>Last Recaptured Fingerprints (<span style="color: green; font-weight: bold">Recapture Count: <% if(last_recapture){ %> ${last_recapture.get(0).get("recapture_count")}<% } %></span> )</h2>

        <div class="row" style="width: 100% !important;">

            <table class="table table-striped" style="width: 100% !important;  margin: 1% !important">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Image Width</th>
                    <th>Image Height</th>
                    <th>Image DPI</th>
                    <th>Image Quality</th>
                    <th>Baseline Comparison</th>
                    <th>Finger Position</th>
                    <th>Date Created</th>
                </tr>
                </thead>


                <tbody>
                <% if (last_recapture) { %>
                <% for (int i = 0; i < last_recapture.size(); i++) {
                    String score = "";
                    String Style = "";
                    String baselineQuality = 0;
                    if(baseline){
                        for (Map<String, Object> baselineRecord : baseline) {
                            if (baselineRecord.containsKey("fingerPosition") && baselineRecord.get("fingerPosition").equals(last_recapture.get(i).get("fingerPosition"))) {
                                baselineQuality = baselineRecord.get("imageQuality") as int;
                                break;
                            }
                        }
                    }

                    if(last_recapture.get(i).get("imageQuality") < baselineQuality) {

                        score = "Higher";
                        Style = "style='background-color: green;'";

                    }else if(last_recapture.get(i).get("imageQuality") == baselineQuality){

                        score = "Equal";
                        Style = "style='background-color: gray;'";
                    }else{
                        score = "Lower";
                        Style = "style='background-color: red;'";
                    }

                %>


                <tr>
                    <td><%= i+1 %></td>
                    <td>${ last_recapture.get(i).get("imageWidth")}</td>
                    <td>${ last_recapture.get(i).get("imageHeight") }</td>
                    <td>${ last_recapture.get(i).get("imageDPI") }</td>
                    <td>${ last_recapture.get(i).get("imageQuality")}</td>
                    <td ${Style}>${score} <small><i><%= baselineQuality %></i></small></td>
                    <td>${ last_recapture.get(i).get("fingerPosition") }</td>
                    <td>${ last_recapture.get(i).get("date_created")}</td>
                </tr>
                <% } %>
                <% } else { %>
                <tr>
                    <td colspan="8">Patient Last Recapture not Found</td>
                </tr>
                <% } %>
                </tbody>
            </table>

        </div>
    </div>
</div>

<hr style="border: solid 2px gray">

<h2>BaseLine Fingerprints</h2>


<div class="row" style="width: 100% !important;">

    <table class="table table-striped" width="100%" style="width: 100% !important;  margin: 1% !important">
        <thead>
        <tr>
            <th>#</th>
            <th>Image Width</th>
            <th>Image Height</th>
            <th>Image DPI</th>
            <th>Image Quality</th>
            <th>Finger Position</th>
            <th>Date Created</th>
        </tr>
        </thead>


        <tbody>
        <% if (baseline) { %>
        <% for (int i = 0; i < baseline.size(); i++) { %>
        <tr>
            <td><%= i+1 %></td>
            <td>${ baseline.get(i).get("imageWidth")}</td>
            <td>${ baseline.get(i).get("imageHeight") }</td>
            <td>${ baseline.get(i).get("imageDPI") }</td>
            <td>${ baseline.get(i).get("imageQuality")}%</td>
            <td>${ baseline.get(i).get("fingerPosition") }</td>
            <td>${ baseline.get(i).get("date_created")}</td>
        </tr>
        <% } %>
        <% } else { %>
        <tr>
            <td colspan="8">Patient Baseline Fingerprint not Found</td>
        </tr>
        <% } %>
        </tbody>
    </table>

</div>
