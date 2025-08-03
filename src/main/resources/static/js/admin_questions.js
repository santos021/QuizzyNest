function updateCorrectAnswerDropdown() {
	    const optionA = document.getElementById('optionA').value.trim();
	    const optionB = document.getElementById('optionB').value.trim();
	    const optionC = document.getElementById('optionC').value.trim();
	    const optionD = document.getElementById('optionD').value.trim();
	
	    const correctAnswerSelect = document.getElementById('correctAnswer');
	
	    // Clear old options
	    correctAnswerSelect.innerHTML = '<option value="" disabled selected>Select correct answer</option>';
	
	    const options = [
	      { label: 'Option A', value: optionA },
	      { label: 'Option B', value: optionB },
	      { label: 'Option C', value: optionC },
	      { label: 'Option D', value: optionD },
	    ];
	
	    options.forEach(opt => {
	      if (opt.value) {
	        const optEl = document.createElement('option');
	        optEl.value = opt.value;
	        optEl.textContent = `${opt.label}: ${opt.value}`;
	        correctAnswerSelect.appendChild(optEl);
	      }
	    });
	  }
	
	  // Update dropdown as user types
	  ['optionA', 'optionB', 'optionC', 'optionD'].forEach(id => {
	    document.getElementById(id).addEventListener('input', updateCorrectAnswerDropdown);
	  });
	
	  // Optionally call once on modal open (for editing mode)
	  document.getElementById('questionModal').addEventListener('shown.bs.modal', updateCorrectAnswerDropdown);
	  
	  document.addEventListener("DOMContentLoaded", function () {
	  	    const deleteButtons = document.querySelectorAll(".delete-btn");
	  	    const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
	  	
	  	    deleteButtons.forEach(button => {
	  	      button.addEventListener("click", function (e) {
	  	        e.preventDefault();
	  	
	  	        const questionId = this.getAttribute("data-id");
	  	        confirmDeleteBtn.href = `/admin/questions/delete/${questionId}`;
	  	
	  	        const deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	  	        deleteModal.show();
	  	      });
	  	    });
	  	  });