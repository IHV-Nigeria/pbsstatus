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

<nav>
    <div class="nav nav-tabs" id="nav-tab" role="tablist">
        <a class="nav-item nav-link" id="nav-all-settings" href="pbsstatus.page">Back to Client List</a>
    </div>
</nav>
<h1>PBS Priority List</h1>


<div class="row" style="width: 100% !important;">

    <table id="filteredPatients" class="table table-striped" style="width: 98% !important;  margin: 1% !important">
        <thead>
        <tr>
            <th>
                PEPFAR ID
            </th>
            <th>
                NDR Match Status
            </th>
            <th>
                Last Recapture Date
            </th>
            <th>
                Baseline Replaced
            </th>
            <th>Remarks</th>
            <th>Action</th>
        </tr>
        </thead>


        <tbody>
        <% if (prioritylist) { %>
        <% for (int i = 0; i < prioritylist.size(); i++) { %>
        <tr>
            <td>${ prioritylist.get(i).get("pepfar_id")}</td>
            <td>${ prioritylist.get(i).get("match_outcome") }</td>
            <td>${ prioritylist.get(i).get("recapture_date") }</td>
            <td>${ prioritylist.get(i).get("baseline_replaced") }</td>
            <td>${ prioritylist.get(i).get("otherinfo") }</td>
            <td><a target="_blank" href="clientpbs.page?pepfarId=${prioritylist.get(i).get("pepfar_id")}&patient_id=0" class="btn btn-outline-info" title="Client to view client PBS Status">View Status</a></td>
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