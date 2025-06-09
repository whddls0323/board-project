document.addEventListener("DOMContentLoaded", function () {
	const commentNicknames = document.querySelectorAll(".left");
	const nowNickname = document.querySelector(".reply-form-user-input").value;
	
	document.querySelectorAll(".commentUpdate,.commentDelete").forEach(button => { //전체 속성 변경
	        button.style.display = "none";
	    });
	
	document.querySelectorAll(".sep").forEach(separator => {
		separator.style.display = "none"; // sep 요소 숨김
	});
	
	commentNicknames.forEach(element => { //하나씩 가져와 조건 확인
		let commentNickname = element.getAttribute("data-comment-nickname");
		if(commentNickname === nowNickname){
			const commentUpdateButton = element.closest(".edge").querySelector(".commentUpdate");
			const commentDeleteButton = element.closest(".edge").querySelector(".commentDelete");
			const separators = element.closest(".edge").querySelectorAll(".sep");
			
			if(commentUpdateButton)
				commentUpdateButton.style.display = "block";	
					
			if (commentDeleteButton) 
				commentDeleteButton.style.display = "block";
			separators.forEach(separator => {
				separator.style.display = "block";
			});
		}
	});
	
    document.querySelectorAll(".commentUpdate").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault(); // 기본 이벤트 차단

            // 클릭된 댓글 정보 가져오기
            const commentElement = event.target.closest(".edge"); // 댓글 div
            const commentId = commentElement.getAttribute("data-comment-id"); // 댓글 ID 가져오기
            const commentBody = commentElement.querySelector(".body").innerText; // 기존 댓글 내용
            const nickname = commentElement.querySelector(".left").innerText; // 작성자 이름
            
            if (!commentId) {
                alert("댓글 ID를 찾을 수 없습니다.");
                return;
            }

            // 기존 수정 폼이 있으면 삭제
            let existingForm = document.querySelector(".edit-form");
            if (existingForm) {
                existingForm.remove();
				return;
            }

            // 수정 폼 생성
            const editForm = document.createElement("form");
            editForm.classList.add("edit-form");

            const container = document.createElement("div");
            container.classList.add("reply-form__container");

            const infoDiv = document.createElement("div");
            infoDiv.classList.add("reply-form__info");

            const userInfoDiv = document.createElement("div");
            userInfoDiv.classList.add("reply-form__user-info");

            const titleSpan = document.createElement("span");
            titleSpan.classList.add("replay-form-title");
            titleSpan.innerText = "댓글 수정";

            const input = document.createElement("input");
            input.classList.add("reply-form-user-input");
            input.type = "text";
            input.disabled = true;
            input.setAttribute("value", nickname);  

            userInfoDiv.appendChild(titleSpan);
            userInfoDiv.appendChild(input);
            infoDiv.appendChild(userInfoDiv);
            container.appendChild(infoDiv);

            const textareaWrapper = document.createElement("div");
            textareaWrapper.classList.add("reply-form-textarea-wrapper");
            
            // textarea를 동적으로 생성 후 추가
            const textarea = document.createElement("textarea");
            textarea.classList.add("reply-form-textarea");
            textarea.name = "comment";
            textarea.maxLength = 8000;
            textarea.required = true;
            textarea.style.height = "99.5px";
            textarea.value = commentBody;

            textareaWrapper.appendChild(textarea);

            const buttonWrapper = document.createElement("div");
            buttonWrapper.classList.add("reply-form__submit-button-wrapper");

            const updateButton = document.createElement("button");
            updateButton.classList.add("reply-form__submit-button");
            updateButton.type = "button";
            updateButton.setAttribute("data-comment-id", commentId);
            updateButton.innerText = "수정 완료";
            
            const updateCommentId = document.createElement("input");
            updateCommentId.type = "hidden";
            updateCommentId.id = "edit-commentId";
            updateCommentId.setAttribute("value",commentId);
            
            const updateBoardId = document.createElement("input");
            updateBoardId.type = "hidden";
            updateBoardId.id = "edit-boardId";
            updateBoardId.setAttribute("value",document.querySelector("#new-comment-board-id").value);
            
            console.log("버튼이 생성됨:", updateButton);
            console.log("버튼의 부모 요소:", buttonWrapper);
			
            
            buttonWrapper.appendChild(updateButton);
            textareaWrapper.appendChild(buttonWrapper);
            container.appendChild(textareaWrapper);
            editForm.appendChild(container);
            editForm.appendChild(updateBoardId);
            editForm.appendChild(updateCommentId);
            
            // 현재 댓글 아래에 수정 폼 삽입
            console.log("수정 폼이 추가될 위치:", commentElement);
            commentElement.after(editForm);
            console.log("수정 폼 부모 요소:", editForm.parentNode);

         	// 수정 폼이 DOM에 추가된 후에 이벤트 리스너를 추가
            editForm.addEventListener('click', function() {
                document.querySelector(".reply-form__submit-button-wrapper").style.display = "flex";              
            });

            // 문서 전체에 클릭 이벤트를 추가하여 버튼을 감추는 로직을 수정
            document.addEventListener('click', function(event) {
                if (!editForm.contains(event.target) && !buttonWrapper.contains(event.target)) {
                    buttonWrapper.style.display = 'none';
                }
            });
            
            // "수정 완료" 버튼 클릭 시 서버에 업데이트 요청 보내기
            updateButton.addEventListener("click", function () {
                const updatedBody = textarea.value;
                const commentId = this.getAttribute("data-comment-id");
				const comment = {
						id: this.getAttribute("data-comment-id"), //dto,entity와 똑같은 이름으로 해야 작동함.
						nickname: input.getAttribute("value"),
	                    body: textarea.value,
	                    boardId: updateBoardId.getAttribute("value")
	                    
				};
				console.log(comment);
				const url = "/api/comments/" + commentId;
                fetch(url, {
                    method: "PATCH",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(comment)
                }).then(response => {
                    const msg = response.ok ? "댓글이 수정되었습니다." : "댓글 수정 실패!";
                    alert(msg);
                    window.location.reload(); // 새로고침하여 반영
                });
            });
        });
    });

    document.querySelectorAll(".commentDelete").forEach(button => {
    	button.addEventListener("click",function(event) {
    		event.preventDefault(); 
    		
    		const commentDeleteBtn = event.target;
    		const commentId = commentDeleteBtn.getAttribute("data-comment-id");
    		
    		const target = commentDeleteBtn.closest(".edge"); 
			const commentNicknames = document.querySelectorAll(".left");
			const nowNickname = document.querySelector(".reply-form-user-input").value;
			commentNicknames.forEach(element => {
				let commentNickname = element.getAttribute("data-comment-nickname");
				if(commentNickname === nowNickname){
					const commentList = element.closest("#comments-list");
					
					const url = "/api/comments/" + commentId;
		    		fetch(url,{
		    			method:"DELETE",
		    		}).then(response => {
		    			
		    			if(!response.ok) {
		    				alert("댓글 삭제 실패.");
		    				throw new Error("댓글 삭제 실패");
		    			}		    				  	
		    		})
		    		.then(() => {
		    			commentList.remove();
		    			
		    			const msg = commentId + "번 댓글을 삭제했습니다.";
		    			alert(msg);
		    			
		    			window.location.reload();
		    		}).catch(error => console.error(error));
				}     
				else{
					console.error("해당 유저가 아닙니다.");
				}				
    		});
    	});
    });
    
    // 댓글 작성 기능 (기존 코드 유지)
    const textarea = document.getElementById('commentTextarea');
    const submitButtonWrapper = document.querySelector('.reply-form__submit-button-wrapper');

    if (textarea) {
        textarea.addEventListener('focus', function() {
            submitButtonWrapper.style.display = 'flex';
        });

        document.addEventListener('click', function(event) {
            if (!textarea.contains(event.target) && !submitButtonWrapper.contains(event.target)) {
                submitButtonWrapper.style.display = 'none';
            }
        });

        const commentCreateBtn = document.querySelector(".reply-form__submit-button");
        if (commentCreateBtn) {
            commentCreateBtn.addEventListener("click", function() {
            	const commentBody = document.querySelector("#commentTextarea").value.trim();
        		
        		if (commentBody === "") { // 댓글 내용이 비어 있는지 확인
        			alert("댓글 내용을 입력해 주세요.");
        			return; // 빈 내용이면 함수 종료
        		}
        		
                const comment = {
                    nickname: document.querySelector(".reply-form-user-input").value,
                    body: document.querySelector("#commentTextarea").value,
                    boardId: document.querySelector("#new-comment-board-id").value
                };
                console.log(comment);

                const url = "/api/board/" + comment.boardId + "/comments";
                fetch(url, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(comment)
                }).then(response => {
                    const msg = (response.ok) ? "댓글이 등록됐습니다." : "댓글 등록 실패..!";
                    alert(msg);
                    window.location.reload();
                });
            });
        }
    }
});