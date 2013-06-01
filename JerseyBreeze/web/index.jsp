<%-- 
    Document   : index
    Created on : May 25, 2013, 10:00:29 PM
    Author     : Josh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="http://code.jquery.com/jquery-2.0.0.min.js"></script>
        <script src="js/q.js"></script>
        <script src="js/knockout-2.1.0.debug.js"></script>
        <script src="js/breeze.debug.js"></script>

    </head>
    <body>
        <script lang="javascript">
            var vo = new breeze.ValidationOptions({
                validateOnSave: false
            });
            var vm = {
                Customers: ko.observableArray([]),
                Manager: new breeze.EntityManager('api/context'),
                Get: function() {
                    var q = new breeze.EntityQuery().from('Customers');
                    vm.Manager.executeQuery(q).then(function(data) {
                        vm.Customers(data.results);
                    }).fail(function(e) {
                        alert(e);
                    });
                },
                Save: function(ent) {
//                            var cust = vm.Manager.createEntity("Customer",{      
//                                
//                            name: 'Vandelay Industries',
//                                discountCode: 'X'
//                            });
                    //ent.entityAspect.markModified();
                    vm.Manager.saveChanges().then(function(data) {
                        //alert(data.results.length + ' record(s) saved.')
                    }).fail(function(e) {
                        alert(e);
                    });
                }
            };
            $(document).ready(function() {
                ko.applyBindings(vm);
                //vm.Manager.setProperties({validationOptions: vo});
                vm.Manager.saveOptions.allowConcurrentSaves = true;
            });
        </script>
        <h1>Hello World!</h1>
        <button id="btnBreeze" data-bind="click: Get">Click Me</button>
        <div id="results" style="border:solid 1px black;" data-bind="foreach: Customers">
            <ul style="list-style-type: none;">
                <li>ID: <input type="text" data-bind="value: customerId" /></li>
                <li>Discount Code: <input type="text" data-bind="value:discountCode" /></li>
                <li>Name: <input type="text" data-bind="value:name"  /></li>
                <li><button data-bind="click: $root.Save">Save</button></li>
            </ul>
        </div>
    </body>
</html>
