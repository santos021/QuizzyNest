document.addEventListener('DOMContentLoaded', function () {
            const contentViews = document.querySelectorAll('.content-view');
            const quizStartModal = new bootstrap.Modal(document.getElementById('quizStartModal'));
            let quizTimerInterval;
            let quizConfig = {};

            function showView(viewId) {
                contentViews.forEach(v => v.classList.remove('active'));
                document.getElementById(viewId + '-view')?.classList.add('active');
                document.querySelectorAll('.nav-link').forEach(l => {
                    l.classList.remove('active');
                    if (l.getAttribute('data-view') === viewId) l.classList.add('active');
                });
            }

            document.querySelectorAll('[data-view]').forEach(link => {
                link.addEventListener('click', e => {
                    e.preventDefault();
                    showView(link.getAttribute('data-view'));
                });
            });

            document.querySelectorAll('[data-view-target]').forEach(button => {
                button.addEventListener('click', e => {
                    e.preventDefault();
                    showView(button.getAttribute('data-view-target'));
                });
            });

            document.getElementById('quizStartModal').addEventListener('show.bs.modal', function (event) {
                const button = event.relatedTarget;
                quizConfig.category = button.getAttribute('data-category');
                quizConfig.time = parseInt(button.getAttribute('data-time'));
                quizConfig.questions = parseInt(button.getAttribute('data-questions'));

                document.getElementById('modal-category-title').textContent = quizConfig.category + " Quiz";
                document.getElementById('modal-question-count').textContent = quizConfig.questions;
                document.getElementById('modal-time-limit').textContent = quizConfig.time;
            });

            document.getElementById('confirm-start-quiz-btn').addEventListener('click', function () {
                quizStartModal.hide();
                startQuiz(quizConfig);
            });

            function startQuiz(config) {
                showView('quiz');
                document.getElementById('quiz-category-title').textContent = config.category + " Quiz";

                let timeRemaining = config.time * 60;

                function updateTimer() {
                    const minutes = Math.floor(timeRemaining / 60);
                    let seconds = timeRemaining % 60;
                    seconds = seconds < 10 ? '0' + seconds : seconds;
                    document.getElementById('quiz-timer').textContent = `${minutes}:${seconds}`;
                    timeRemaining--;

                    if (timeRemaining < 0) {
                        clearInterval(quizTimerInterval);
                        alert("Time's up!");
                        finishQuiz();
                    }
                }

                clearInterval(quizTimerInterval);
                quizTimerInterval = setInterval(updateTimer, 1000);
                updateTimer();
            }

            function finishQuiz() {
                clearInterval(quizTimerInterval);
                // In a real app, you'd calculate the actual score here.
                showView('quiz-result');
            }

            document.getElementById('submit-quiz-btn').addEventListener('click', finishQuiz);
        });