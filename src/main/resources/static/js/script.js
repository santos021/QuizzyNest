let currentQuestion = 0;
    const totalQuestions = document.querySelectorAll('[id^="question-"]').length;

    document.getElementById('next-btn').addEventListener('click', () => {
        document.getElementById(`question-${currentQuestion}`).classList.add('d-none');
        currentQuestion++;
        document.getElementById(`question-${currentQuestion}`).classList.remove('d-none');
        updateButtons();
    });

    document.getElementById('prev-btn').addEventListener('click', () => {
        document.getElementById(`question-${currentQuestion}`).classList.add('d-none');
        currentQuestion--;
        document.getElementById(`question-${currentQuestion}`).classList.remove('d-none');
        updateButtons();
    });

    function updateButtons() {
        document.getElementById('prev-btn').disabled = currentQuestion === 0;
        document.getElementById('next-btn').classList.toggle('d-none', currentQuestion === totalQuestions - 1);
        document.getElementById('submit-btn').classList.toggle('d-none', currentQuestion !== totalQuestions - 1);
    }
    
 // === Quiz Timer ===
    const timerDisplay = document.getElementById("timer");
    const quizForm = document.getElementById("quizForm");

    let totalTimeInSeconds = 5 * 60; // 10 minutes

    function startTimer() {
        const timerInterval = setInterval(() => {
            let minutes = Math.floor(totalTimeInSeconds / 60);
            let seconds = totalTimeInSeconds % 60;
            timerDisplay.textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

            if (totalTimeInSeconds <= 0) {
                clearInterval(timerInterval);
                alert("Time's up! Your quiz will be submitted.");
                quizForm.submit();
            }

            totalTimeInSeconds--;
        }, 1000);
    }

    // Start countdown when page loads
    window.onload = () => {
        startTimer();
        showQuestion(currentQuestionIndex);
    };