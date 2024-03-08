<%
    ui.includeJavascript("pbsstatus", "jquery-3.3.1.js")
    ui.includeJavascript("pbsstatus", "jquery.dataTables.min.js")
    ui.includeJavascript("pbsstatus", "datatables.min.js")
    ui.includeJavascript("pbsstatus", "buttons.flash.min.js")
    ui.includeJavascript("pbsstatus", "jquery-ui.js")
    ui.includeJavascript("pbsstatus", "jszip.min.js")
    ui.includeJavascript("smsreminder", "myAjax.js")
    ui.includeJavascript("pbsstatus", "pdfmake.min.js")
    ui.includeJavascript("pbsstatus", "vfs_fonts.js")
    ui.includeCss("pbsstatus", "bootstrap.min.css")
    ui.includeJavascript("pbsstatus", "bootstrap.min.js")
    ui.includeJavascript("pbsstatus", "buttons.html5.min.js")
    ui.includeJavascript("pbsstatus", "buttons.print.min.js")
    ui.includeJavascript("pbsstatus", "datatable.button.min.js")
    ui.includeCss("pbsstatus", "buttons.dataTables.min.css")
    ui.includeCss("pbsstatus", "jquery.dataTables.min.css")
    ui.includeCss("pbsstatus", "myStyle.css")
    def id = config.id
%>


<h1>Clients PBS Status</h1>


<div class="row" style="width: 100% !important;">

    <table id="filteredPatients" class="table table-striped" style="width: 98% !important;  margin: 1% !important">
        <thead>
        <tr>
            <th>
                PEPFAR ID
            </th>
            <th>
                First Name
            </th>
            <th>
                Last Name
            </th>
            <th>
                View PBS Status
            </th>
        </tr>
        </thead>


        <tbody>
        <% if (patients) { %>
        <% for (int i = 0; i < patients.size(); i++) { %>
            <tr>
                <td>${ patients.get(i).get("pepfarId")}</td>
                <td>${ patients.get(i).get("given_name") }</td>
                <td>${ patients.get(i).get("family_name") }</td>
                <td><a target="_blank" href="clientpbs.page?pepfarId=${patients.get(i).get("pepfarId")}&patient_id=${patients.get(i).get("patient_id")}" class="btn btn-outline-info" title="Client to view client PBS Status">View Status</a></td>
            </tr>
            <% } %>
        <% } else { %>
            <tr>
                <td colspan="4">No Patients Found</td>
            </tr>
        <% } %>
        </tbody>
    </table>

</div>

<script type="text/javascript">


    var jq = jQuery;

    jq(function () {

        jq('#filteredPatients thead tr').clone(true).appendTo('#filteredPatients thead');
        jq('#filteredPatients thead tr:eq(1) th:not(:last)').each(function(i) {
            var title = jq(this).text();
            jq(this).html('<input type="text" class="form-control" placeholder="Search ' + title + '" value="" style="width: 100%;" />');

            jq('input', this).on('keyup change', function() {
                if (table.column(i).search() !== this.value) {
                    table
                        .column(i)
                        .search(this.value)
                        .draw();
                }
            });
        });


        var table = jq('#filteredPatients').DataTable({
            orderCellsTop: true,
            fixedHeader: true,
            "order": [
                [0, "asc"]
            ],
            "paging": false,
            "pageLength": 50,
            "filter": true,
            "ordering": true,
            deferRender: true,
            dom: 'Bfrtip',
            buttons: [
                'copy', 'csv', 'excel', 'pdf', 'print'
            ]
        });
    });
</script>