/*
 * ANGULARJS 
 */

angular.module('leads', [])

	/*MAIN CONTROLLER*/
	.controller('MainController', function($scope, $http, $timeout) {

	var local_server_url = "http://localhost:8080";
	$scope.showLeads=true
	$scope.leads=[]
	$scope.lead={}
	$scope.eventTypes=['BEGIN','SCHEDULE_VISIT','MARK_AS_VISITED','CREATE_PROPOSAL','SEND_PROPOSAL','BEGIN_WORK','FINISH_WORK','SEND_INVOICE','RECEIVE_PAYMENT','END_LEAD']
	$scope.totalLeads=0
	$scope.currentProposal = {'lines':{}}
	var pageRange = 0
	$scope.filter = ''
	var lines = 100

	
   $scope.saveProposal = function(lead, proposal) {
			//console.log(proposal.lead)
			var its = [];
			proposal.items.forEach(item=>{
					var lns = [];
					item.lines.split('\n').forEach(line=>{ 
							lns.push({'description': line});
					})
					its.push({'title': item.title, 'lines': lns});
			})
			proposal.items=its
			proposal.lines=null
			
	    $http.post(local_server_url + "/proposals?leadId="+lead.id, proposal).then(function(response){
	    	$scope.lead.proposals.push(response.data)
	    	//proposal = {}
	    	$scope.currentProposal = {'items':[{}]}
	    	//$scope.showLeadDetails(lead)
	    }, function(response){
	    	console.log(response)
	    });
	    
    }

    $scope.currentProposal = {
      callMissUtility : true,
      items : [ {} ]
    };

    $scope.encreaseItem = function() {
	    $scope.currentProposal.items.push({})
    }
	
	$scope.scheduleVisit = function(lead, time){
    $http.post(local_server_url + "/leads/" + lead.id + "/schedulevisit", time).then(function(response){
    	lead.visit = response.data
    	$scope.showLeadDetails(lead)
    }, function(response){
    	console.log(response)
    	alert("Error: " + response.status + ". Make sure the date format is right.")
    });
	}
	
	$scope.exibitLeads = function(){
		$scope.showLeads=true
		$scope.lead_details_bottom_page = 'lead-details-nav-tabs.html';
	}
	
	$scope.fireEvent = function(lead, event){
    $http.post(local_server_url + "/leads/" + lead.id + "/fireevent", event).then(function(response){
    	lead.event = event
    	$scope.showLeadDetails(lead)
    }, function(response){
    	console.log(response)
    });
	}
	
	var findNextEvents = function(lead){
    $http.get(local_server_url + "/leads/" + lead.id + "/nextevents").then(function(response){
    	lead.nextEvents=response.data
    }, function(response){
    	console.log(response)
    });
	}
	
	$scope.sendProposalByMail = function(proposal, lead){
    $http.post(local_server_url + "/proposals/" + proposal.id + "/lead/" + lead.id, proposal).then(function(response){
    }, function(response){
    	console.log(response)
    });
	}
	
	$scope.showLeadDetails = function (lead){
		$scope.showLeads=false
		$scope.lead = lead
		findNextEvents(lead)
		findNextEventLogs(lead)
	}
	
	var findNextEventLogs = function(lead){
    $http.get(local_server_url + "/leads/"+lead.id+"/eventlogs").then(function(response){
    	lead.eventLogs = response.data
    }, function(response){
    	console.log(response)
    });
	}

	var findTotalLeads = function(filter) {
    $scope.filter = filter
    $http.get(local_server_url + "/leads/total?eventType=" + $scope.filter)
        .then(function(response) {
	        $scope.totalLeads = response.data
	    		defineBolEol()
        }, function(response) {
	        console.log(response)
        });
  }

	var findLeads = function(pageRange, lines,     filter    ){
	    $http.get(local_server_url + "/leads?pageRange="+pageRange+"&lines="+lines+"&eventType="+filter).then(function(response){
	    	$scope.leads = response.data
	    	findTotalLeads(filter)
	    }, function(response){
	    	console.log(response)
	    });
	  }
	
	$scope.filterLeads = function(filter){
    if (!filter)
	    filter = ''
		$scope.filter = filter
		pageRange = 0
		findLeads(pageRange, lines, filter)
	}
	
	$scope.drop = function(){
    $http.get(local_server_url + "/leads/drop").then(function(response){
    	findLeads(-1, lines, $scope.filter);
    }, function(response){
    	console.log(response)
    });
	}

	        
	$scope.BOL = true
	$scope.EOL = false
  $scope.leadsGetNext = function() {
		if (!$scope.EOL) {
	    pageRange++
	    findLeads(pageRange, lines, $scope.filter);
	  }
		defineBolEol()
	}
	

	$scope.leadsGetPrevious = function() {
    if (pageRange > 0 ) {
	    pageRange--
	    findLeads(pageRange, lines, $scope.filter);
    }
    defineBolEol()
  }
	
	var defineBolEol = function(){
    $scope.BOL = (pageRange < 1)
    $scope.EOL = $scope.totalLeads / lines - pageRange < 1
	}

	findLeads(pageRange, lines, $scope.filter);
	
} ) ;

