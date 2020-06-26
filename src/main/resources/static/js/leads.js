/*
 * ANGULARJS 
 */

angular.module('leads', [])

  /*MAIN CONTROLLER*/
  .controller('MainController', function ($scope, $http, $timeout) {

    //var local_server_url = "http://localhost:8080";
    var local_server_url = "";
    $scope.showLeads = true
    $scope.leads = []
    $scope.lead = {}
    $scope.eventTypes = ['BEGIN', 'CONTACT_QUALIFY', 'ASSIGN_TO_ESTIMATOR', 'SCHEDULE_VISIT', 'MARK_AS_VISITED', 'CREATE_PROPOSAL', 'SEND_PROPOSAL', 'BEGIN_WORK', 'FINISH_WORK', 'SEND_INVOICE', 'RECEIVE_PAYMENT', 'END_LEAD']
    $scope.totalLeads = 0
    var pageRange = 0
    $scope.filter = ''
    var lines = 100
    $scope.originalLines = []
    var errorMessage = 'An error occured.'
    $scope.searchable_text = ''
   	$scope.menu = 'menu.html';
    $scope.body = 'leads-list.html';
    $scope.newLead = {'client':{}}
    $scope.errorMessages = []
    $scope.successMessages = []
    $scope.currentUser = {}
    $scope.currentCompany = {}
    $scope.users = []
    $scope.companies = [];
    

    
    $scope.newCurrentCompany = function(currentUser){
    	$scope.currentCompany = {'id':-1};
    }
    
    $scope.resetCurrentCompany = function(currentUser){
    	$scope.currentCompany = {};
    }
    
    $scope.saveCompany = function(company){
  		$http.put(local_server_url + '/companies', company ).then(function (response) {
  			$scope.currentCompany = response.data
  			successMessage(company.name + ' updated.')
  		}, function (response) {
  			errorMessage(response.data)
  		});
    }
    
    $scope.searchCompany = function(companyName){
    	if(companyName.length > 2){
    		$http.get(local_server_url + '/companies/byName?companyName=' + companyName ).then(function (response) {
    			$scope.companies = response.data
    		}, function (response) {
    			errorMessage(response.data)
    		});
    	}
    }

    $scope.chooseCompany = function(company){
    	$scope.currentCompany = company;
    	$scope.companies = [];
    }


    
    
    $scope.searchCompanies = function(){
    	if(!$scope.companies || $scope.companies.length <= 0){
	  		$http.get(local_server_url + '/companies' ).then(function (response) {
	  			$scope.companies = response.data
	  		}, function (response) {
	  			errorMessage(response.data)
	  		});
    	}
    }
    
    $scope.newCurrentUser = function(currentUser){
    	$scope.currentUser = {'id':-1};
    }
    
    $scope.resetCurrentUser = function(currentUser){
    	$scope.currentUser = {};
    }
    
    $scope.saveUser = function(user){
  		$http.put(local_server_url + '/users', user ).then(function (response) {
  			$scope.currentUser = response.data
  			successMessage(user.name + ' updated.')
  		}, function (response) {
  			errorMessage(response.data)
  		});
    }
    
    $scope.searchUser = function(userName){
    	if(!$scope.companies || $scope.companies.length <= 0){
    		$scope.searchCompanies();
    	}
    	if(userName.length > 2){
    		$http.get(local_server_url + '/users?userName=' + userName ).then(function (response) {
    			$scope.users = response.data
    		}, function (response) {
    			errorMessage(response.data)
    		});
    	}
    }

    $scope.chooseUser = function(user){
    	$scope.currentUser = user;
    	$scope.users = [];
    }

    var resetMessages = function(){
      $scope.errorMessages = []
      $scope.successMessages = []
    }

    var errorMessage = function(msg){
      $scope.errorMessages = [msg]
    }

    var successMessage = function(msg){
      $scope.successMessages = [msg]
    }

    $scope.sendHiringDecisionEmail = function(lead, client){
    	if (confirm('Send e-mail asking about '+client.name+'\'s  hiring decision ? ')) {
    		$http.get(local_server_url + '/clients/'+client.id+'/leads/'+lead.id+'/hiringdecision').then(function (response) {
    			successMessage('"Ask for Hiring Decision E-mail" sent to '+ client.name)
          findNextEventLogs(lead)
    		}, function (response) {
    			console.log(response)
    			errorMessage(response.data)
    		});
    	}
    }
    
    $scope.sendCantReachEmail = function(lead, client){
    	if (confirm('Send Can\'t Reach E-mail to '+client.name+' ? ')) {
    		$http.get(local_server_url + '/clients/'+client.id+'/leads/'+lead.id+'/cantreach').then(function (response) {
    			successMessage('"Can\'t Reach E-mail" sent to '+ client.name)
    			findNextEventLogs(lead)
    		}, function (response) {
    			console.log(response)
    			errorMessage(response.data)
    		});
    	}
    }
    
    var getLeadTypes = function(){
  		$http.get(local_server_url + '/leads/types').then(function (response) {
  			$scope.newLead.types = response.data
  		}, function (response) {
  			console.log(response)
        errorMessage(response.data)
  		});
    }
    
    $scope.newLeadSave = function(newLead){
  		$http.post(local_server_url + '/leads', newLead).then(function (response) {
  			$scope.leads.unshift(response.data)
  			$scope.totalLeads++
  			$("button[data-dismiss=\"modal\"]"). click()
  			successMessage('New lead saved')
  		}, function (response) {
  			console.log(response)
        errorMessage(response.data)
  		});
    }
    
    $scope.newLeadCancel = function(){
    	$scope.newLead.clients = {}
      $scope.newLead = {'client':{}}
    }
    
    $scope.chooseClient= function(client){
    	$scope.newLead.client = client;
    	$scope.newLead.clients = {}
    }
    
    $scope.searchClient = function(clientName){
    	if( clientName.length > 2){
    		$http.get(local_server_url + '/clients?name=' + clientName).then(function (response) {
    			$scope.newLead.clients = response.data
    		}, function (response) {
    			console.log(response)
    			errorMessage(response.data)
    		});
    	}else{
    		$scope.newLead.clients = {}
    	}
    }

    $scope.updateClient = function(client){
      $http.put(local_server_url + '/clients', client).then(function (response) {
      	$scope.lead.client = response.data
        successMessage(client.name+'\'s information has been updated')
      }, function (response) {
        console.log(response)
        errorMessage(response.data)
      });
    }
    
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
    			errorMessage(response.data)
        });
    	}
    }
    	
    $scope.saveNote = function(lead, note){
      $http.post(local_server_url + '/leads/' + lead.id + '/addNote', note).then(function (response) {
      	$scope.lead.notes = response.data.notes
      	note = ''
      }, function (response) {
        console.log(response)
        errorMessage(response.data)
      });
    }

    $scope.emailProposal = function (client, proposal, lead) {
    	if (confirm('Send Proposal #'+proposal.number+' to '+client.name+' via E-mail ? ')) {
    		$http.get(local_server_url + '/proposals/' + proposal.id + '/email').then(function (response) {
    			proposal.emailed = true;
    			proposal.finished = true;
    			successMessage('Proposal #' + proposal.number + ' sent to ' + client.name)
    			findNextEventLogs(lead)
    		}, function (response) {
    			console.log(response)
    			errorMessage(response.data)
    		});
    	}
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
        errorMessage(response.data)
      });
    }

    $scope.editProposal = function (proposal) {
    	$scope.currentProposal = convertToClientFormat(proposal);
      angular.element(document.querySelector('#oiwk4397849jj9')).click()
    }

    $scope.saveProposal = function (lead, proposal) {
    	var save = function(lead, proposal){
    		if(!lead.proposals)
    			lead.proposals=[]
    		lead.proposals = lead.proposals.filter(function (value, index, arr) { return value.id != proposal.id; })
    		var prop = convertToServerFormat(proposal)
    		$http.post(local_server_url + "/proposals?leadId=" + lead.id, prop).then(function (response) {
    			lead.proposals = lead.proposals.filter(function (value, index, arr) { return value != prop; })
    			$scope.lead.proposals.push(response.data)
    			$scope.currentProposal = $scope.createNewProposal()
    			angular.element(document.querySelector('#oiwk4397849jj9')).click()
    		}, function (response) {
    			console.log(response)
    			errorMessage(response.data)
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
      resetMessages()
    }

    $scope.fireEvent = function (lead, event) {
      $http.post(local_server_url + "/leads/" + lead.id + "/fireevent", event).then(function (response) {
        lead.event = event
        $scope.showLeadDetails(lead)
      }, function (response) {
        console.log(response)
        errorMessage(response.data)
      });
    }

    var findNextEvents = function (lead) {
      $http.get(local_server_url + "/leads/" + lead.id + "/nextevents").then(function (response) {
        lead.nextEvents = response.data
        return response.data
      }, function (response) {
        console.log(response)
        errorMessage(response.data)
      });
    }

    $scope.sendProposalByMail = function (proposal, lead) {
      $http.post(local_server_url + "/proposals/" + proposal.id + "/lead/" + lead.id, proposal).then(function (response) {
      }, function (response) {
        console.log(response)
        errorMessage(response.data)
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
    			errorMessage(response.data)
        });
    }

    var findLeads = function (pageRange, lines, filter) {
      $http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&eventType=" + filter).then(function (response) {
        $scope.leads = response.data
        findTotalLeads(filter)
        $scope.leads.forEach(function(l, index){ findNextEvents(l)})
      }, function (response) {
        console.log(response)
        errorMessage(response.data)
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
        errorMessage(response.data)
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

    $scope.reload = function(){
    	findLeads(pageRange, lines, $scope.filter);
      $scope.searchable_text = ''
      getLeadTypes();
    }
    
    $scope.reload();
    setInterval(function(){
  		$http.head(local_server_url).then(function (response) {
  			if(response.status != 200){
  				location.reload()
  			}
  		}, function (response) {
  			location.reload()
  		});
    }, 2000);
    
    $scope.searchCompanies();
    
  });

