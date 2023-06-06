// Utility function
function Util () {};

/* 
	class manipulation functions
*/
Util.hasClass = function(el, className) {
	return el.classList.contains(className);
};

Util.addClass = function(el, className) {
	var classList = className.split(' ');
 	el.classList.add(classList[0]);
 	if (classList.length > 1) Util.addClass(el, classList.slice(1).join(' '));
};

Util.removeClass = function(el, className) {
	var classList = className.split(' ');
	el.classList.remove(classList[0]);	
	if (classList.length > 1) Util.removeClass(el, classList.slice(1).join(' '));
};

Util.toggleClass = function(el, className, bool) {
	if(bool) Util.addClass(el, className);
	else Util.removeClass(el, className);
};

Util.setAttributes = function(el, attrs) {
  for(var key in attrs) {
    el.setAttribute(key, attrs[key]);
  }
};

/* 
  DOM manipulation
*/
Util.getChildrenByClassName = function(el, className) {
  var children = el.children,
    childrenByClass = [];
  for (var i = 0; i < children.length; i++) {
    if (Util.hasClass(children[i], className)) childrenByClass.push(children[i]);
  }
  return childrenByClass;
};

Util.is = function(elem, selector) {
  if(selector.nodeType){
    return elem === selector;
  }

  var qa = (typeof(selector) === 'string' ? document.querySelectorAll(selector) : selector),
    length = qa.length,
    returnArr = [];

  while(length--){
    if(qa[length] === elem){
      return true;
    }
  }

  return false;
};
// File#: _2_pricing-table
// Usage: codyhouse.co/license
(function() {
	// NOTE: you need the js code only when using the --has-switch variation of the pricing table
	// default version does not require js
	var pTable = document.getElementsByClassName('js-p-table--has-switch');
	if(pTable.length > 0) {
		for(var i = 0; i < pTable.length; i++) {
			(function(i){ addPTableEvent(pTable[i]);})(i);
		}

		function addPTableEvent(element) {
			var pSwitch = element.getElementsByClassName('js-p-table__switch')[0];
			if(pSwitch) {
				pSwitch.addEventListener('change', function(event) {
          Util.toggleClass(element, 'p-table--yearly', (event.target.value == 'yearly'));
				});
			}
		}
	}
}());

(function() {
  function setFaqState(faq, isOpen) {
    var question = faq.querySelector('.ques');
    var answer = faq.querySelector('.ans');
    if (!question || !answer) return;

    faq.classList.toggle('is-open', isOpen);
    question.setAttribute('aria-expanded', String(isOpen));
    answer.setAttribute('aria-hidden', String(!isOpen));
    answer.style.maxHeight = isOpen ? answer.scrollHeight + 'px' : '0px';
  }

  function toggleFaq(faq) {
    setFaqState(faq, !faq.classList.contains('is-open'));
  }

  function initFaqs() {
    var faqs = document.querySelectorAll('.faqs');
    if (!faqs.length) return;

    faqs.forEach(function(faq) {
      var question = faq.querySelector('.ques');
      if (!question) return;

      setFaqState(faq, faq.classList.contains('is-open'));
      question.addEventListener('click', function() {
        toggleFaq(faq);
      });
      question.addEventListener('keydown', function(event) {
        if (event.key === 'Enter' || event.key === ' ') {
          event.preventDefault();
          toggleFaq(faq);
        }
      });
    });

    window.addEventListener('resize', function() {
      faqs.forEach(function(faq) {
        if (faq.classList.contains('is-open')) setFaqState(faq, true);
      });
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initFaqs);
  } else {
    initFaqs();
  }
}());
