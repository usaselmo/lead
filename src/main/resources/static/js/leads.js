/*
 * ANGULARJS 
 */

angular.module('leads', [])

  /*MAIN CONTROLLER*/
  .controller('MainController', function ($scope, $http, $timeout) {

    var local_server_url = "http://localhost:8080";
    $scope.showLeads = true
    $scope.leads = []
    $scope.lead = {}
    $scope.eventTypes = ['BEGIN', 'CONTACT_QUALIFY', 'SCHEDULE_VISIT', 'MARK_AS_VISITED', 'CREATE_PROPOSAL', 'SEND_PROPOSAL', 'BEGIN_WORK', 'FINISH_WORK', 'SEND_INVOICE', 'RECEIVE_PAYMENT', 'END_LEAD']
    $scope.totalLeads = 0
    var pageRange = 0
    $scope.filter = ''
    var lines = 100
    $scope.originalLines = []
    var errorMessage = 'An error occured.'
    $scope.searchable_text = ''
   	$scope.menu = 'menu.html';
    $scope.body = 'leads-list.html';
    	
    $scope.changeBody = function(bodyName){
    	$scope.body = bodyName;
    }
    
    $scope.search = function(text){
    	$scope.searchable_text = text
    	if(!text){
    		findLeads(pageRange, lines, $scope.filter);
    	}
    	else if(text.length > 2 ){
        $http.get(local_server_url + "/leads/search?text=" + text).then(function (response) {
          $scope.leads = response.data
          $scope.totalLeads = response.data.length
          $scope.filter = ''
        }, function (response) {
          console.log(response)
        });
    	}
    }
    	
    $scope.saveNote = function(lead, note){
      $http.post(local_server_url + '/leads/' + lead.id + '/addNote', note).then(function (response) {
      	$scope.lead.notes = response.data.notes
      }, function (response) {
        console.log(response)
        alert(response.data)
      });
    }

    $scope.emailProposal = function (proposal) {
      $http.get(local_server_url + '/proposals/' + proposal.id + '/email').then(function (response) {
        proposal.emailed = true;
        proposal.finished = true;
      }, function (response) {
        console.log(response)
        alert(response.data)
      });
    }


    $scope.createNewProposal = function () {
      var prop = {
        'items': [{}],
        'callMissUtility': true,
        'scopeOfWork': 'Concrete work as per visit',
        'paymentSchedule': '1. 50% down payment upon start of the project\n2. 50% final payment upon completion of the project.',
        'workWarranty': 'All new material and labor are guaranteed for 36 months from completion date. All work to be completed in a neat and workmanlike manner. Warranty applies for concrete cracks that are 3/8" or greater in separation or height difference for flat concrete work. Warranty excludes the following: concrete damage caused by deicers such as salt or any deicer or fertilizer containing ammonium nitrate or ammonium sulfate, concrete spider cracks, hairline cracks and color variance.'
      }
      $scope.currentProposal = prop;
      return prop;
    }

    $scope.currentProposal = $scope.createNewProposal();

    $scope.deleteProposal = function (lead, proposal, saveProp) {
      $http.delete(local_server_url + "/proposals?leadId=" + lead.id + '&proposalId=' + proposal.id).then(function (response) {
        lead.proposals = lead.proposals.filter(function (value, index, arr) { return value != proposal; })
        if(saveProp)
        	saveProp(lead, proposal)
      }, function (response) {
        console.log(response)
        alert(response.data)
      });
    }

    $scope.editProposal = function (proposal) {
    	$scope.currentProposal = convertToClientFormat(proposal);
      angular.element(document.querySelector('#oiwk4397849jj9')).click()
    }

    $scope.saveProposal = function (lead, proposal) {
    	var save = function(lead, proposal){
    		lead.proposals = lead.proposals.filter(function (value, index, arr) { return value.id != proposal.id; })
    		var prop = convertToServerFormat(proposal)
    		$http.post(local_server_url + "/proposals?leadId=" + lead.id, prop).then(function (response) {
    			lead.proposals = lead.proposals.filter(function (value, index, arr) { return value != prop; })
    			$scope.lead.proposals.push(response.data)
    			$scope.currentProposal = $scope.createNewProposal()
    			angular.element(document.querySelector('#oiwk4397849jj9')).click()
    		}, function (response) {
    			console.log(response)
    		});
    	}
    	if(proposal.id)
    		$scope.deleteProposal(lead, proposal, save);
    	else
    		save(lead, proposal)
    }

    var convertToClientFormat = function (proposal) {
      prop = copy(proposal)
      items = []
      prop.items.forEach(item => {
        item.lines = item.lines.map(line => {
          $scope.originalLines[item.id + line.description] = line.id;
          return line.description;
        }).join("\n")
        items.push(item)
      })
      prop.items = items;
      return prop;
    }

    var copy = function (obj) {
      return JSON.parse(JSON.stringify(obj))
    }

    var convertToServerFormat = function (proposal) {
      prop = copy(proposal)
      var its = [];
      let ols = $scope.originalLines
      prop.items.forEach(item => {
        var lns = [];
        item.lines.split('\n').forEach(line => {
          lns.push({ 'description': line, 'id': ols[item.id + line] });
        })
        its.push({ 'title': item.title, 'lines': lns, 'price': item.price });
      })
      prop.items = its
      prop.lines = null
      prop.note = proposal.note
      $scope.originalLines=[]
      return prop
    }

    $scope.encreaseItem = function () {
      $scope.currentProposal.items = $scope.currentProposal.items.concat([{ 'id': '' }])
      console.log($scope.currentProposal.items)
    }

    $scope.removeItem = function (proposal) {
      $scope.currentProposal.items.pop()
    }

    $scope.scheduleVisit = function (lead, time) {
      $http.post(local_server_url + "/leads/" + lead.id + "/schedulevisit", time).then(function (response) {
        lead.visit = response.data
        $scope.showLeadDetails(lead)
      }, function (response) {
        console.log(response)
        alert("Error: " + response.status + ". Make sure the date format is right.")
      });
    }

    $scope.exibitLeads = function () {
      $scope.showLeads = true
      $scope.lead_details_bottom_page = 'lead-details-nav-tabs.html';
    }

    $scope.fireEvent = function (lead, event) {
      $http.post(local_server_url + "/leads/" + lead.id + "/fireevent", event).then(function (response) {
        lead.event = event
        $scope.showLeadDetails(lead)
      }, function (response) {
        console.log(response)
      });
    }

    var findNextEvents = function (lead) {
      $http.get(local_server_url + "/leads/" + lead.id + "/nextevents").then(function (response) {
        lead.nextEvents = response.data
      }, function (response) {
        console.log(response)
      });
    }

    $scope.sendProposalByMail = function (proposal, lead) {
      $http.post(local_server_url + "/proposals/" + proposal.id + "/lead/" + lead.id, proposal).then(function (response) {
      }, function (response) {
        console.log(response)
      });
    }

    $scope.showLeadDetails = function (lead) {
      $scope.showLeads = false
      $scope.lead = lead
      findNextEvents(lead)
      findNextEventLogs(lead)
    }

    var findNextEventLogs = function (lead) {
      $http.get(local_server_url + "/leads/" + lead.id + "/eventlogs").then(function (response) {
        lead.eventLogs = response.data
      }, function (response) {
        console.log(response)
      });
    }

    var findTotalLeads = function (filter) {
      $scope.filter = filter
      $http.get(local_server_url + "/leads/total?eventType=" + $scope.filter)
        .then(function (response) {
          $scope.totalLeads = response.data
          defineBolEol()
        }, function (response) {
          console.log(response)
        });
    }

    var findLeads = function (pageRange, lines, filter) {
    	console.log('pageRange:'+pageRange+' lines:'+lines+' filter:'+filter)
      $http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&eventType=" + filter).then(function (response) {
        $scope.leads = response.data
        findTotalLeads(filter)
      }, function (response) {
        console.log(response)
      });
    }

    $scope.filterLeads = function (filter) {
      if (!filter)
        filter = ''
      $scope.filter = filter
      pageRange = 0
      findLeads(pageRange, lines, filter)
    }

    $scope.drop = function () {
      $http.get(local_server_url + "/leads/drop").then(function (response) {
        findLeads(-1, lines, $scope.filter);
      }, function (response) {
        console.log(response)
      });
    }


    $scope.BOL = true
    $scope.EOL = false
    $scope.leadsGetNext = function () {
      if (!$scope.EOL) {
        pageRange++
        findLeads(pageRange, lines, $scope.filter);
      }
      defineBolEol()
    }


    $scope.leadsGetPrevious = function () {
      if (pageRange > 0) {
        pageRange--
        findLeads(pageRange, lines, $scope.filter);
      }
      defineBolEol()
    }

    var defineBolEol = function () {
      $scope.BOL = (pageRange < 1)
      $scope.EOL = $scope.totalLeads / lines - pageRange < 1
    }

    findLeads(pageRange, lines, $scope.filter);

  });

