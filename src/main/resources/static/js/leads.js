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
	var pageRange = 0
	$scope.filter = ''
	var lines = 100
	$scope.originalLines = []
	var errorMessage = 'An error occured.'
  
  $scope.emailProposal = function(proposal){
    $http.get(local_server_url + '/proposals/'+proposal.id+'/email').then(function(response){
      proposal.emailed = true;
    }, function(response){
      console.log(response)
      alert(response.data)
    });
  }

  
	$scope.createNewProposal = function(){
    console.log('creating new blank proposal')
    var prop = {
      'items':[{}], 
			'callMissUtility': true,
			'scopeOfWork': 'Concrete work as per visit',
			'paymentSchedule':'1. 50% down payment upon start of the project\n2. 50% final payment upon completion of the project.', 
			'workWarranty': 'All new material and labor are guaranteed for 36 months from completion date. All work to be completed in a neat and workmanlike manner. Warranty applies for concrete cracks that are 3/8" or greater in separation or height difference for flat concrete work. Warranty excludes the following: concrete damage caused by deicers such as salt or any deicer or fertilizer containing ammonium nitrate or ammonium sulfate, concrete spider cracks, hairline cracks and color variance.'
    }
    $scope.currentProposal = prop;
    return prop;
	}
	
  $scope.currentProposal = $scope.createNewProposal();
		
	$scope.reDoProposal = function(lead, proposal){
    $http.delete(local_server_url + "/proposals?leadId="+lead.id+'&proposalId='+proposal.id).then(function(response){
    	lead.proposals = lead.proposals.filter(function(value, index, arr){ return value != proposal;})
    	$scope.currentProposal = convertToClientFormat(proposal);
    }, function(response){
      console.log(response)
      alert(response.data)
    });
    
  }
  
  var convertToClientFormat = function(proposal){
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

  var copy = function(obj){
    return JSON.parse(JSON.stringify(obj))
  }

  var convertToServerFormat = function(proposal){
    prop = copy(proposal)
    var its = [];
    let ols = $scope.originalLines
    prop.items.forEach(item=>{
         var lns = [];
         item.lines.split('\n').forEach(line=>{
           lns.push({'description': line, 'id':ols[item.id+line] });
         })
         its.push({'title': item.title, 'lines': lns});
     })
     prop.items=its
     prop.lines=null
     return prop
  }
	
  $scope.saveProposal = function(lead, proposal) {
    var prop = convertToServerFormat(proposal)
    $http.post(local_server_url + "/proposals?leadId="+lead.id, prop).then(function(response){
      $scope.lead.proposals.push(response.data)
      $scope.currentProposal = $scope.createNewProposal()
      angular.element(document.querySelector('#oiwk4397849jj9')).click()
    }, function(response){
      console.log(response)
    });
	    
  }

    $scope.currentProposal = {
      callMissUtility : true,
      items : [ {} ]
    };

    $scope.encreaseItem = function() {
    	console.log($scope.currentProposal.items)
	    $scope.currentProposal.items = $scope.currentProposal.items.concat([{'id':''}])
	    console.log($scope.currentProposal.items)
	  }

    $scope.removeItem = function() {
	    $scope.currentProposal.items.pop()
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

	var findLeads = function(pageRange, lines, filter){
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

